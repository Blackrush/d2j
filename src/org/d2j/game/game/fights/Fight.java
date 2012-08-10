package org.d2j.game.game.fights;

import org.d2j.common.CollectionUtils;
import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.EndActionTypeEnum;
import org.d2j.common.client.protocol.enums.FightStateEnum;
import org.d2j.common.client.protocol.enums.FightTypeEnum;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.fights.actions.Turn;
import org.d2j.game.game.fights.buffs.BuffMaker;
import org.d2j.game.game.fights.buffs.EffectContainerBuff;
import org.d2j.game.game.pathfinding.Pathfinding;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.SpellException;
import org.d2j.game.game.spells.cell_effects.CellEffect;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.model.Map;
import org.d2j.game.model.Spell;
import org.d2j.game.model.WeaponItem;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.*;

/**
 * User: Blackrush
 * Date: 14/11/11
 * Time: 20:36
 * IDE : IntelliJ IDEA
 */
public abstract class Fight {
    private long id;
    protected List<Turn> turns;
    private FightStateEnum state;
    private Instant startFight;

    protected final IGameConfiguration configuration;
    protected final Map map;
    protected final FightCell[] cells;
    protected final Team challengers, defenders;
    protected Collection<IFighter> fighters;

    public Fight(IGameConfiguration configuration, Map map) {
        this.id = map.getNextFightId();
        this.configuration = configuration;
        this.map = map;
        this.cells = FightCell.toFightCell(this, map.getCells(), map.getPlaces());

        String[] places = map.getPlaces().split("\\|");
        this.challengers = new Team(this, FightTeamEnum.CHALLENGER, places[0]);
        this.defenders = new Team(this, FightTeamEnum.DEFENDER, places[1]);

        this.state = FightStateEnum.INIT;
    }

    public abstract int remainingTime();
    public abstract FightTypeEnum getFightType();

    public long getId() {
        return id;
    }

    public IGameConfiguration getConfiguration() {
        return configuration;
    }

    public Map getMap() {
        return map;
    }

    public FightStateEnum getState() {
        return state;
    }

    public FightCell[] getCells() {
        return cells;
    }

    public Collection<IFighter> getFighters(){
        return fighters;
    }

    public Team getTeam(FightTeamEnum teamType){
        switch (teamType){
            case CHALLENGER:
                return challengers;
            case DEFENDER:
                return defenders;
            default:
                return null;
        }
    }

    public Team getChallengers() {
        return challengers;
    }

    public Team getDefenders() {
        return defenders;
    }

    public Turn getCurrentTurn(){
        return turns.get(0);
    }

    public Duration getFightDuration(){
        return new Duration(startFight, Instant.now());
    }

    protected abstract void onInited() throws FightException;

    public abstract boolean startIfYouCan() throws FightException;
    protected abstract void onStarted() throws FightException;

    public abstract boolean stopIfYouCan() throws FightException;
    protected abstract void stopFight(FightStateEnum oldState, FightTeamEnum winnerTeam) throws FightException;

    public abstract void onQuit(IFighter fighter) throws FightException;
    public abstract void onClosed(IFighter fighter) throws FightException;

    public void init() throws FightException {
        if (state != FightStateEnum.INIT){
            throw new FightException("Fight's state doesn't allow this request.");
        }

        state = FightStateEnum.PLACE;

        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFightJoin(
                        challengers,
                        defenders
                );
            }
        });

        map.addFight(this);

        onInited();
    }

    protected void start() throws FightException {
        state = FightStateEnum.ACTIVE;

        generateTurns();
        fighters = CollectionUtils.concat(challengers.getFighters(), defenders.getFighters());

        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFightStart(turns, fighters);
            }
        });

        onStarted();

        startFight = Instant.now();

        getCurrentTurn().begin();
    }

    public void stop() throws FightException {
        if (state != FightStateEnum.PLACE && state != FightStateEnum.ACTIVE){
            throw new FightException("Invalid request: fight's state doesn't allow this request.");
        }

        FightStateEnum oldState = state;
        state = FightStateEnum.FINISHED;

        if (oldState == FightStateEnum.ACTIVE){
            getCurrentTurn().end();
        }

        map.removeFight(this);

        stopFight(oldState, FightUtils.getWinnerTeam(challengers, defenders));
    }

    public void nextTurn() throws FightException {
        if (state == FightStateEnum.FINISHED){
            return;
        }
        else if (state != FightStateEnum.ACTIVE){
            throw new FightException("Invalid request: fight's state doesn't allow this request.");
        }

        if (!challengers.isAlive() || !defenders.isAlive()){
            stop();
        }
        else{
            onTurnStopped();

            Turn turn = turns.remove(0);
            if (!turn.hasAbandoned() && turn.getFighter().isAlive()){
                turns.add(turn);
            }

            while (getCurrentTurn().hasAbandoned()){
                turns.remove(0);
            }

            turns.get(0).begin();
        }
    }

    public void notifyReady(final IFighter fighter) throws FightException {
        if (state != FightStateEnum.PLACE){
            throw new FightException("Fight's state doesn't allow this request.");
        }

        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFighterReady(fighter);
            }
        });
    }

    public void notifyFighterPlacement(final IFighter fighter) throws FightException {
        if (state != FightStateEnum.PLACE){
            throw new FightException("Fight's state doesn't allow this request.");
        }

        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFighterPlacement(fighter);
            }
        });
    }

    public void notifyFighterQuit(final IFighter fighter) throws FightException {
        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyFighterQuit(fighter);
            }
        });
    }

    public void notifyAddFighter(final IFighter fighter) throws FightException {
        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyAddFighter(fighter);
            }
        });
    }

    public void notifyClientMessage(final IFighter fighter, final String message) throws FightException {
        foreach(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyClientMessage(fighter, message);
            }
        });
    }

    public void notifyTeamClientMessage(final IFighter fighter, final String message) throws FightException {
        foreachTeam(fighter.getTeam().getTeamType(), new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTeamClientMessage(fighter, message);
            }
        });
    }

    protected void onTurnStopped() throws FightException {
        AppendableFightHandlerAction action = new AppendableFightHandlerAction();
        for (IFighter fighter : fighters) {
            fighter.getGlyphes().onTurnStopped(action);
        }
        foreach(action);
    }

    public void castSpell(final Spell spell, final IFighter caster, short targetCellId, FightLog logs) throws FightException {
        final FightCell targetCell = cells[targetCellId];
        final ISpellLevel infos = spell.getInfos();
        final int distance = Pathfinding.distanceBetween(caster.getCurrentCell().getPosition(), targetCell.getPosition());

        if (caster.getStatistics().get(CharacteristicType.ActionPoints).getTotal() < infos.getCost()){
            throw new SpellException("Invalid request: not enough AP.");
        }
        else if (distance > infos.getMaxRange() || distance < infos.getMinRange()){
            throw new SpellException("Invalid request: you're too close or too far from the target.");
        }
        else if (caster.getLogs().castBySpell(spell.getTemplate()).size() > infos.getMaxPerTurn()){
            throw new SpellException("Invalid request: too many cast of this spell.");
        }
        else{
            caster.getHandler().notifyRefreshStatistics();
            AppendableFightHandlerAction action = new AppendableFightHandlerAction(
                    new FightHandlerAction(){
                        @Override
                        public void call(IFightHandler obj) throws FightException {
                            obj.notifyStartAction(caster.getId());
                        }
                    }
            );

            boolean failure  = FightUtils.computeFailure(infos.getCriticalFailRate(), caster),
                    critical = infos.getCriticalEffects().size() >0 && !failure && FightUtils.computeCritical(infos.getCriticRate(), caster);

            if (failure){
                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyBasicAction(
                                ActionTypeEnum.SPELL_FAILURE,
                                caster,
                                spell.getTemplate().getId()
                        );
                    }
                });
            }
            else{
                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyCastSpell(
                                caster,
                                spell,
                                targetCell
                        );
                    }
                });

                if (critical){
                    action.append(new FightHandlerAction() {
                        @Override
                        public void call(IFightHandler obj) throws FightException {
                            obj.notifyBasicAction(
                                    ActionTypeEnum.SPELL_CRITICAL,
                                    caster,
                                    spell.getTemplate().getId()
                            );
                        }
                    });
                }

                if (targetCell.getCurrentFighter() != null &&
                    caster.getLogs().castBySpell(spell.getTemplate())
                                    .castByTarget(targetCell.getCurrentFighter())
                                    .size() > infos.getMaxPerPlayer())
                {
                    throw new SpellException("Invalid request: too many cast of this spell on the same target.");
                }

                Collection<Effect> effects = critical ? infos.getCriticalEffects() : infos.getEffects();

                for (Effect effect : effects) {
                    List<IFighter> alreadyApplied = new ArrayList<>();

                    Collection<FightCell> targetCells = effect.getZone()
                            .filter(caster.getCurrentCell(), targetCell, cells, map.getWidth(), map.getHeight());

                    for (FightCell cell : targetCells)
                    {
                        if (cell.getCurrentFighter() != null){
                            if (alreadyApplied.contains(cell.getCurrentFighter())) continue;
                            alreadyApplied.add(cell.getCurrentFighter());
                        }

                        if (effect.getNbTurns() > 0)
                        {
                            if (cell.getCurrentFighter() != null)
                            {
                                if (!effect.getFilter().filter(caster, cell.getCurrentFighter())) continue;
                                if (effect instanceof BuffMaker)
                                {
                                    cell.getCurrentFighter().getBuffs().add(((BuffMaker) effect).make(caster, cell.getCurrentFighter()));
                                }
                                else
                                {
                                    cell.getCurrentFighter().getBuffs().add(new EffectContainerBuff(
                                            caster,
                                            cell.getCurrentFighter(),
                                            spell.getTemplate(),
                                            effect
                                    ));
                                }
                            }
                            else
                            {
                                //todo: trap, glyph
                            }
                        }
                        else
                        {
                            effect.apply(action, caster, cell);
                        }
                    }
                }

                if (targetCell.getCurrentFighter() != null){
                    logs.add(new FightLog.SpellCast(targetCell.getCurrentFighter(), spell.getTemplate()));
                }
            }

            caster.getStatistics().get(CharacteristicType.ActionPoints).addContext((short) -infos.getCost());

            action.append(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyBasicAction(
                            ActionTypeEnum.AP_CHANGEMENT,
                            caster,
                            (int) caster.getId(),
                            -infos.getCost()
                    );
                    obj.notifyEndAction(EndActionTypeEnum.SPELL, caster);
                }
            });

            foreach(action);
        }
    }

    public void useWeapon(final WeaponItem weapon, final IFighter caster, short targetCellId) throws FightException {
        final FightCell targetCell = cells[targetCellId];
        int distance = Pathfinding.distanceBetween(caster.getCurrentCell().getPosition(), targetCell.getPosition());

        if (caster.getStatistics().get(CharacteristicType.ActionPoints).getTotal() < weapon.getTemplate().getCost()){
            throw new FightException("Invalid request: not enough AP.");
        }
        else if (distance < weapon.getTemplate().getMinRange() || distance > weapon.getTemplate().getMaxRange()){
            throw new FightException("Invalid request: too close or to far from target.");
        }
        else{
            caster.getHandler().notifyRefreshStatistics();
            AppendableFightHandlerAction action = new AppendableFightHandlerAction(
                    new FightHandlerAction(){
                        @Override
                        public void call(IFightHandler obj) throws FightException {
                            obj.notifyStartAction(caster.getId());
                        }
                    }
            );

            boolean failure  = FightUtils.computeFailure(weapon.getTemplate().getCriticalFailureRate(), caster),
                    critical = !failure && FightUtils.computeCritical(weapon.getTemplate().getCriticalRate(), caster);

            if (failure){
                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyBasicAction(
                                ActionTypeEnum.SPELL_FAILURE,
                                caster,
                                0
                        );
                    }
                });
            }
            else{
                action.append(new FightHandlerAction() {
                    @Override
                    public void call(IFightHandler obj) throws FightException {
                        obj.notifyUseWeapon(
                                caster,
                                targetCell
                        );
                    }
                });

                if (critical){
                    action.append(new FightHandlerAction() {
                        @Override
                        public void call(IFightHandler obj) throws FightException {
                            obj.notifyBasicAction(
                                    ActionTypeEnum.SPELL_CRITICAL,
                                    caster,
                                    0
                            );
                        }
                    });
                }

                Collection<Effect> effects = critical ? weapon.getWeaponEffects() : weapon.getWeaponEffectsCritic();
                Collection<FightCell> targetCells = (critical ? weapon.getZone() : weapon.getCriticalZone())
                        .filter(caster.getCurrentCell(), targetCell, cells, map.getWidth(), map.getHeight());

                for (Effect effect : effects){
                    for (FightCell cell : targetCells){
                        effect.apply(action, caster, cell);
                    }
                }
            }

            caster.getStatistics().get(CharacteristicType.ActionPoints).addContext((short) -weapon.getTemplate().getCost());

            action.append(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyBasicAction(
                            ActionTypeEnum.AP_CHANGEMENT,
                            caster,
                            (int) caster.getId(),
                            -weapon.getTemplate().getCost()
                    );
                    obj.notifyEndAction(EndActionTypeEnum.SPELL, caster);
                }
            });

            foreach(action);
        }
    }

    public void useFists(final IFighter caster, short targetCellId) throws FightException {
        final FightCell targetCell = cells[targetCellId];
        int distance = Pathfinding.distanceBetween(caster.getCurrentCell().getPosition(), targetCell.getPosition());

        if (caster.getStatistics().get(CharacteristicType.ActionPoints).getTotal() < 4){
            throw new FightException("Invalid request: not enough AP.");
        }
        else if (distance != 1){
            throw new FightException("Invalid request: too close or to far from target.");
        }
        else{
            caster.getHandler().notifyRefreshStatistics();
            AppendableFightHandlerAction action = new AppendableFightHandlerAction(
                    new FightHandlerAction(){
                        @Override
                        public void call(IFightHandler obj) throws FightException {
                            obj.notifyStartAction(caster.getId());
                        }
                    }
            );
            action.append(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyUseWeapon(
                            caster,
                            targetCell
                    );
                }
            });

            Fighter.FISTS_EFFECT.get().apply(action, caster, targetCell);

            caster.getStatistics().get(CharacteristicType.ActionPoints).addContext((short) -5);

            action.append(new FightHandlerAction() {
                @Override
                public void call(IFightHandler obj) throws FightException {
                    obj.notifyBasicAction(
                            ActionTypeEnum.AP_CHANGEMENT,
                            caster,
                            (int) caster.getId(),
                            -5
                    );
                    obj.notifyEndAction(EndActionTypeEnum.SPELL, caster);
                }
            });

            foreach(action);
        }
    }

    public void applyCellEffects(IFighter trigger, FightCell cell) throws FightException {
        if (cell.getEffects().size() <= 0) return;

        AppendableFightHandlerAction action = new AppendableFightHandlerAction();

        for (int i = 0; i < cell.getEffects().size(); i++) { // avoid ConcurrentModificationException
            CellEffect effect = cell.getEffects().get(i);
            effect.onFighterWalkedOn(action, trigger);
            effect.delete();
        }

        foreach(action);
    }

    public IFighter getFighter(long id){
        IFighter fighter;
        if ((fighter = challengers.getFighter(id)) != null) return fighter;
        if ((fighter = defenders.getFighter(id)) != null) return fighter;
        return null;
    }

    public void foreach(FightHandlerAction action) throws FightException {
        if (state == FightStateEnum.ACTIVE){
            for (IFighter fighter : fighters){
                action.call(fighter.getHandler());
            }
        }
        else{
            for (IFighter fighter : challengers){
                action.call(fighter.getHandler());
            }
            for (IFighter fighter : defenders){
                action.call(fighter.getHandler());
            }
        }
    }

    public void foreachTeam(FightTeamEnum team, FightHandlerAction action) throws FightException {
        switch (team) {
            case CHALLENGER:
                for (IFighter fighter : challengers){
                    action.call(fighter.getHandler());
                }
                break;

            case DEFENDER:
                for (IFighter fighter : defenders){
                    action.call(fighter.getHandler());
                }
                break;

            case SPECTATOR:
                break;
        }
    }

    private void generateTurns(){
        turns = new ArrayList<>();

        for (IFighter fighter : challengers){
            turns.add(new Turn(fighter, this));
        }
        for (IFighter fighter : defenders){
            turns.add(new Turn(fighter, this));
        }

        Collections.sort(turns, new Comparator<Turn>() {
            @Override
            public int compare(Turn o1, Turn o2) {
                return o2.getFighter().getStatistics().get(CharacteristicType.Initiative).getTotal() -
                        o1.getFighter().getStatistics().get(CharacteristicType.Initiative).getTotal();
            }
        });
    }
}
