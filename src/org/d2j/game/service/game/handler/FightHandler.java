package org.d2j.game.service.game.handler;

import org.d2j.common.CollectionUtils;
import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.*;
import org.d2j.common.client.protocol.enums.*;
import org.d2j.game.game.Cell;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.fights.actions.FightMovement;
import org.d2j.game.game.fights.actions.PlayerFightMovement;
import org.d2j.game.game.fights.actions.Turn;
import org.d2j.game.game.fights.buffs.Buff;
import org.d2j.game.game.spells.ISpell;
import org.d2j.game.game.spells.cell_effects.Glyph;
import org.d2j.game.game.spells.cell_effects.Trap;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.d2j.game.service.game.GameClientHandler;
import org.d2j.game.service.game.GameService;
import org.d2j.utils.Selector;

import java.util.Collection;
import java.util.Observable;

/**
 * User: Blackrush
 * Date: 15/11/11
 * Time: 18:59
 * IDE : IntelliJ IDEA
 */
public class FightHandler extends GameClientHandler implements IFightHandler {
    private final Fight fight;
    private final Fighter fighter;
    private final Team team;
    private final RolePlayHandler rolePlay;

    public FightHandler(GameService service, GameClient client, Fight fight, FightTeamEnum teamType, RolePlayHandler rolePlay) throws FightException {
        super(service, client);

        this.fight = fight;
        this.team = this.fight.getTeam(teamType);
        this.fighter = this.team != null ? new Fighter(this.fight, this.client, this) : null;
        if (this.team != null){
            this.team.addFighter(this.fighter);
        }

        this.rolePlay = rolePlay;
    }

    @Override
    public void parse(String packet) throws Exception {
        String[] args;
        switch (packet.charAt(0)){
            case 'B':
                switch (packet.charAt(1)){
                    case 'A':
                        rolePlay.parse(packet);
                        break;

                    case 'M':
                        if (client.getAccount().isMuted()){
                            client.getTchatOut().error("Un Maître de Jeu vous a coupé la parole.");
                        }
                        else{
                            args = packet.substring(2).split("\\|");
                            if (args[0].length() > 1){
                                Character target = service.getWorld().getRepositoryManager().getCharacters().findByName(args[0]);
                                rolePlay.parseClientPrivateRequestMessage(target, args[1]);
                            }
                            else{
                                parseClientMultiRequestMessage(ChannelEnum.valueOf(packet.charAt(2)), args[1]);
                            }
                        }
                        break;
                }
                break;

            case 'f':
                switch (packet.charAt(1)){
                    case 'H':
                    case 'N':
                    case 'P':
                    case 'S':
                        parseFightSetAttributeRequestMessage(
                                FightAttributeType.valueOf(packet.charAt(1))
                        );
                        break;
                }
                break;

            case 'G':
                switch (packet.charAt(1)){
                    case 'A':
                        parseGameActionRequestMessage(
                                ActionTypeEnum.valueOf(Integer.parseInt(packet.substring(2, 5))),
                                packet.substring(5)
                        );
                        break;

                    case 'K':
                        client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
                        break;

                    case 'p':
                        parseFighterPlacementRequestMessage(Short.parseShort(packet.substring(2)));
                        break;

                    case 'R':
                        parseFighterReadyRequestMessage(packet.charAt(2) == '1');
                        break;

                    case 'Q':
                        parseQuitFightRequestMessage(
                                packet.length() > 2 ?
                                        fight.getFighter(Long.parseLong(packet.substring(2))) :
                                        fighter
                        );
                        break;

                    case 't':
                        parseTurnEndRequestMessage();
                        break;
                }
                break;

            case 'O':
                switch (packet.charAt(1)){
                    case 'M':
                        parseItemMovementRequestMessage(packet);
                        break;
                }
                break;
        }
    }

    @Override
    public void onClosed() throws Exception {
        rolePlay.onClosed();
        fight.onClosed(fighter);
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    @Override
    public void notifyFightJoin(Team challengers, Team defenders) throws FightException {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(FightGameMessageFormatter.newFightMessage(
                    fight.getState(),
                    fight.getFightType() == FightTypeEnum.CHALLENGE,
                    fight.getFightType() == FightTypeEnum.CHALLENGE,
                    team.getTeamType() == FightTeamEnum.SPECTATOR,
                    fight.remainingTime(),
                    fight.getFightType()
            ));

            buf.append(FightGameMessageFormatter.startCellsMessage(
                    challengers.getPlaces(),
                    defenders.getPlaces(),
                    team.getTeamType()
            ));

            if (!challengers.isEmpty()){
                buf.append(FightGameMessageFormatter.showFightersMessage(
                        Fighter.toBaseFighterType(challengers.getFighters())
                ));
            }

            if (!defenders.isEmpty()){
                buf.append(FightGameMessageFormatter.showFightersMessage(
                        Fighter.toBaseFighterType(defenders.getFighters())
                ));
            }
        }
    }

    @Override
    public void notifyFighterReady(IFighter fighter) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterReadyMessage(
                fighter.getId(),
                fighter.isReady()
        ));
    }

    @Override
    public void notifyAddFighter(IFighter fighter) throws FightException {
        client.getSession().write(FightGameMessageFormatter.
                showFighterMessage(fighter.toBaseFighterType())
        );
    }

    @Override
    public void notifyRemoveFighter(long fighterId) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterQuitMessage(fighterId));
    }

    @Override
    public void notifyAddFighters(Collection<IFighter> fighters) throws FightException {
        client.getSession().write(FightGameMessageFormatter.
                showFightersMessage(Fighter.toBaseFighterType(fighters))
        );
    }

    @Override
    public void notifyFighterPlacement(IFighter fighter) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterPlacementMessage(
                fighter.getId(),
                fighter.getCurrentCell().getId(),
                fighter.getCurrentOrientation()
        ));
    }

    @Override
    public void notifyFighterQuit(IFighter f) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterQuitMessage(f.getId()));
    }

    @Override
    public void notifyQuit() throws FightException {
        client.setHandler(rolePlay);
        client.getCharacter().getCurrentMap().addObserver(rolePlay);

        client.getSession().write(FightGameMessageFormatter.fighterLeftMessage());
    }

    @Override
    public void notifyFightStart(Collection<Turn> turns, Collection<IFighter> fighters) throws FightException {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(FightGameMessageFormatter.fightersPlacementMessage(
                    Fighter.toBaseFighterType(fighters)
            ));

            buf.append(FightGameMessageFormatter.fightStartMessage());

            buf.append(FightGameMessageFormatter.turnListMessage(CollectionUtils.select(turns, new Selector<Turn, Long>() {
                    @Override
                    public Long select(Turn in) {
                        return in.getFighter().getId();
                    }
            })));

            buf.append(FightGameMessageFormatter.fighterInformationsMessage(
                    Fighter.toBaseFighterType(fighters)
            ));
        }
    }

    @Override
    public void notifyTurnStart(Turn turn) throws FightException {
        client.getSession().write(FightGameMessageFormatter.turnStartMessage(
                turn.getFighter().getId(),
                turn.remainingTime().getMillis()
        ));
    }

    @Override
    public void notifyTurnStop(Turn turn) throws FightException {
        client.getSession().write(FightGameMessageFormatter.
                turnEndMessage(turn.getFighter().getId()));
    }

    @Override
    public void notifyFighterMovement(FightMovement movement) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterMovementMessage(
                movement.getFighter().getId(),
                movement.getPath()
        ));
    }

    @Override
    public void notifyBasicAction(ActionTypeEnum actionType, IFighter fighter, int arg) throws FightException {
        client.getSession().write(FightGameMessageFormatter.actionMessage(
                actionType,
                fighter.getId(),
                arg
        ));
    }

    @Override
    public void notifyBasicAction(ActionTypeEnum actionType, IFighter fighter, int arg1, int arg2) throws FightException {
        client.getSession().write(FightGameMessageFormatter.actionMessage(
                actionType,
                fighter.getId(),
                arg1,
                arg2
        ));
    }

    @Override
    public void notifyEndAction(EndActionTypeEnum action, IFighter fighter) throws FightException {
        client.getSession().write(FightGameMessageFormatter.endFightActionMessage(
                action,
                fighter.getId()
        ));
    }

    @Override
    public void notifyRefreshStatistics() throws FightException {
        client.getSession().write(fighter.getStatistics().getStatisticsMessage());
    }

    @Override
    public void notifyFightersInformations(Collection<IFighter> fighters) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fighterInformationsMessage(
                Fighter.toBaseFighterType(fighters)
        ));
    }

    @Override
    public void notifyStartAction(long fighterId) throws FightException {
        client.getSession().write(FightGameMessageFormatter.startActionMessage(fighterId));
    }

    @Override
    public void notifyCastSpell(IFighter fighter, ISpell spell, FightCell target) throws FightException {
        client.getSession().write(FightGameMessageFormatter.castSpellActionMessage(
                fighter.getId(),
                spell.getTemplate().getId(),
                spell.getTemplate().getSprite(),
                spell.getTemplate().getSpriteInfos(),
                spell.getLevel(),
                target.getId()
        ));
    }

    @Override
    public void notifyUseWeapon(IFighter fighter, FightCell target) throws FightException {
        client.getSession().write(FightGameMessageFormatter.actionMessage(
                ActionTypeEnum.MELEE_ATACK,
                fighter.getId(),
                target.getId()
        ));
    }

    @Override
    public void notifyTeleportation(IFighter fighter, FightCell target) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_CHANGEMENT,
                fighter.getId(),
                fighter.getId(),
                target.getId()
        ));
    }

    @Override
    public void notifySlide(IFighter caster, IFighter target) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CELL_SLIDE,
                caster.getId(),
                target.getId(),
                target.getCurrentCell().getId()
        ));
    }

    @Override
    public void notifyTransposition(IFighter caster, IFighter target) throws FightException {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(FightGameMessageFormatter.fightActionMessage(
                    ActionTypeEnum.CELL_CHANGEMENT,
                    caster.getId(),
                    caster.getId(),
                    caster.getCurrentCell().getId()
            ));
            buf.append(FightGameMessageFormatter.fightActionMessage(
                    ActionTypeEnum.CELL_CHANGEMENT,
                    caster.getId(),
                    target.getId(),
                    target.getCurrentCell().getId()
            ));
        }
    }

    @Override
    public void notifyClearBuffs(IFighter caster, IFighter target) throws FightException {
        client.getSession().write(FightGameMessageFormatter.fightActionMessage(
                ActionTypeEnum.CLEAR_BUFFS,
                caster.getId(),
                target.getId()
        ));
    }

    @Override
    public void notifyInvisible(IFighter caster, IFighter target, boolean add, int remainingTurns) throws FightException {
        if (add) {
            client.getSession().write(FightGameMessageFormatter.actionMessage(
                    ActionTypeEnum.INVISIBLE,
                    caster.getId(),
                    target.getId(),
                    remainingTurns
            ));
        }
        else {
            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                buf.append(FightGameMessageFormatter.actionMessage(
                        ActionTypeEnum.INVISIBLE,
                        caster.getId(),
                        target.getId()
                ));
                buf.append(FightGameMessageFormatter.actionMessage(
                        ActionTypeEnum.CELL_CHANGEMENT,
                        caster.getId(),
                        target.getId(),
                        target.getCurrentCell().getId()
                ));
            }
        }
    }

    @Override
    public void notifyNewBuff(IFighter fighter, Buff buff) {
        client.getSession().write(FightGameMessageFormatter.fighterBuffMessage(
                fighter.getId(),
                buff.getEffect(),
                buff.getValue1(),
                buff.getValue2(),
                buff.getValue3(),
                buff.getChance(),
                buff.getRemainingTurns(),
                buff.getSpell().getId()
        ));
    }

    @Override
    public void notifyFightEnd(Team winners, Team losers) {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(FightGameMessageFormatter.fightEndMessage(
                    fight.getFightDuration().getMillis(),
                    fight.getFightType() == FightTypeEnum.AGRESSION,
                    winners.getLeader().toBaseEndFighterType(),
                    Fighter.toBaseEndFighterType(winners.getFighters()),
                    Fighter.toBaseEndFighterType(losers.getFighters())
            ));

            rolePlay.join(buf);
        }

        client.setHandler(rolePlay);
    }

    @Override
    public void notifyClientMessage(IFighter fighter, String message) {
        client.getSession().write(ChannelGameMessageFormatter.clientMultiMessage(
                ChannelEnum.General.toChar(),
                fighter.getId(),
                fighter.getName(),
                message
        ));
    }

    @Override
    public void notifyTeamClientMessage(IFighter fighter, String message) {
        client.getSession().write(ChannelGameMessageFormatter.clientMultiMessage(
                ChannelEnum.Team.toChar(),
                fighter.getId(),
                fighter.getName(),
                message
        ));
    }

    @Override
    public void notifyUpdateAttribute(FightAttributeType attribute, boolean active) {
        client.getSession().write(InfoGameMessageFormatter.fightAttributeActivationMessage(attribute, active));
    }

    @Override
    public void notifyNoAction() throws FightException {
        client.getSession().write(GameMessageFormatter.noActionMessage());
    }

    @Override
    public void notifyTrapUsed(IFighter trigger, Trap trap) throws FightException {
        client.getSession().write(FightGameMessageFormatter.trapUsedMessage(
                trigger.getId(),
                trap.getOriginalSpell().getId(),
                trigger.getCurrentCell().getId(),
                trap.getCaster().getId()
        ));
    }

    @Override
    public void notifyTrapDeleted(Trap trap) throws FightException {
        if (trap.getCaster().getTeam() == team) {
            client.getSession().write(FightGameMessageFormatter.trapDeletedMessage(
                    trap.getCaster().getId(),
                    trap.getBaseCell().getId(),
                    trap.getZone().getSize()
            ));
        }

        if (trap.getCaster() == fighter){
            client.getSession().write(FightGameMessageFormatter.localTrapDeleteMessage(
                    fighter.getId(),
                    trap.getBaseCell().getId()
            ));
        }
    }

    @Override
    public void notifyGlyphAdded(Glyph glyph) throws FightException {
        client.getSession().write(FightGameMessageFormatter.glyphAddedMessage(
                glyph.getCaster().getId(),
                glyph.getBaseCell().getId(),
                glyph.getZone().getSize(),
                glyph.getColor()
        ));
    }

    @Override
    public void notifyGlyphDeleted(Glyph glyph) throws FightException {
        client.getSession().write(FightGameMessageFormatter.glyphDeletedMessage(
                glyph.getCaster().getId(),
                glyph.getBaseCell().getId(),
                glyph.getZone().getSize(),
                glyph.getColor()
        ));
    }

    @Override
    public void notifyTrapAdded(Trap trap) throws FightException {
        if (trap.getCaster().getTeam() == team) {
            client.getSession().write(FightGameMessageFormatter.trapAddedMessage(
                    trap.getCaster().getId(),
                    trap.getBaseCell().getId(),
                    trap.getZone().getSize()
            ));
        }

        if (trap.getCaster() == fighter) {
            client.getSession().write(FightGameMessageFormatter.localTrapAddedMessage(
                    trap.getCaster().getId(),
                    trap.getBaseCell().getId()
            ));
        }
    }

    private void parseClientMultiRequestMessage(ChannelEnum channel, String message) throws Exception {
        switch (channel){
            case General:
                fight.notifyClientMessage(fighter, message);
                break;

            case Team:
                fight.notifyTeamClientMessage(fighter, message);
                break;

            default:
                rolePlay.parseClientMultiRequestMessage(channel, message);
                break;
        }
    }

    private void parseFighterPlacementRequestMessage(short cellId) throws Exception {
        FightCell cell = fight.getCells()[cellId];

        if (!cell.isAvailable()){
            throw new Exception("Bad request: unavailable selected cell.");
        }
        else if (cell.getStartCell() != team.getTeamType()) {
            throw new Exception("Bad request: selected cell isn't a begin cell.");
        }
        else if (fighter.isReady()){
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
        }
        else {
            fighter.getCurrentCell().setCurrentFighter(null);

            fighter.setCurrentCell(cell);
            fighter.getCurrentCell().setCurrentFighter(fighter);

            fight.notifyFighterPlacement(fighter);
        }
    }

    private void parseFighterReadyRequestMessage(boolean ready) throws FightException {
        fighter.setReady(ready);
        fight.notifyReady(fighter);

        fight.startIfYouCan();
    }

    private void parseQuitFightRequestMessage(IFighter target) throws Exception {
        if (fighter != target && !fighter.isLeader()) {
            throw new Exception("You are not the leader.");
        }
        else if (target == null){
            throw new Exception("Unknown fighter.");
        }
        else{
            fight.onQuit(target);
        }
    }

    private void parseGameActionRequestMessage(ActionTypeEnum action, String args) throws Exception {
        if (fight.getState() != FightStateEnum.ACTIVE){
            throw new Exception("Bad request: fight's state don't allow you to do this action.");
        }

        if (fight.getCurrentTurn().getFighter() != fighter){
            throw new Exception("Bad request: it isn't your turn!");
        }

        switch (action){
            case MOVEMENT:
                parseMovementRequestMessage(args);
                break;

            case CAST_SPELL:
                String[] data = args.split(";");
                parseCastSpellRequestMessage(
                        fighter.getSpells().get(Integer.parseInt(data[0])),
                        Short.parseShort(data[1])
                );
                break;

            case MELEE_ATACK:
                parseCastMeleeRequestMessage(
                        Short.parseShort(args)
                );
                break;
        }
    }

    private void parseCastSpellRequestMessage(Spell spell, short targetCellId) throws Exception {
        if (spell == null){
            throw new Exception("Invalid request: unknown spell or not learned.");
        }
        else if (fight.getCurrentTurn().getFighter() != fighter){
            throw new Exception("Invalid request: it isn't your turn.");
        }
        else if (fighter.getCurrentAction() != null){
            throw new Exception("You are busy!");
        }
        else{
            fight.castSpell(spell, fighter, targetCellId, fighter.getLogs());
        }
    }

    private void parseCastMeleeRequestMessage(short targetCell) throws Exception {
        if (fight.getCurrentTurn().getFighter() != fighter){
            throw new Exception("Bad request: it isn't your turn.");
        }

        if (client.getCharacter().getBag().has(ItemPositionEnum.Weapon)){
            WeaponItem weapon = (WeaponItem)client.getCharacter().getBag().get(ItemPositionEnum.Weapon);
            fight.useWeapon(weapon, fighter, targetCell);
        }
        else{
            fight.useFists(fighter, targetCell);
        }
    }

    private void parseMovementRequestMessage(String path) throws Exception {
        if (path.equals("")){
            throw new Exception("Bad request: empty path.");
        }
        else if (!fighter.isAlive()){
            throw new Exception("Bad request: you're dead!");
        }
        else if (fighter.getCurrentAction() != null){
            throw new Exception("You are busy!");
        }

        PlayerFightMovement movement = new PlayerFightMovement(
                fight,
                fighter,
                fight.getCells()[Cell.decode(path.substring(path.length() - 2))]
        );

        if (movement.init()){
            movement.begin();
        }
    }

    private void parseTurnEndRequestMessage() throws Exception {
        if (fight.getCurrentTurn().getFighter() != fighter){
            client.getSession().write(BasicGameMessageFormatter.noOperationMessage());
        }
        else{
            fight.getCurrentTurn().end();
        }
    }

    private void parseFightSetAttributeRequestMessage(FightAttributeType attribute) throws FightException {
        if (attribute == FightAttributeType.DENY_SPECTATORS && fight.getState() == FightStateEnum.PLACE) return;

        fight.getMap().updateFightFlag(fighter.getTeam().getLeader(), attribute, team.setAttribute(attribute));
    }

    private void parseItemMovementRequestMessage(String packet) throws Exception {
        if (fight.getState() != FightStateEnum.PLACE){
            throw new Exception("Current fight's state does not allow you to perform this action");
        }
        else{
            if (fighter.isReady()){
                fighter.setReady(false);
                fight.notifyReady(fighter);
            }
            rolePlay.parse(packet);
        }
    }
}
