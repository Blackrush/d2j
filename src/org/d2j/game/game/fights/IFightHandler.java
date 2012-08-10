package org.d2j.game.game.fights;

import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.EndActionTypeEnum;
import org.d2j.common.client.protocol.enums.FightAttributeType;
import org.d2j.game.game.fights.actions.FightMovement;
import org.d2j.game.game.fights.actions.Turn;
import org.d2j.game.game.fights.buffs.Buff;
import org.d2j.game.game.spells.ISpell;
import org.d2j.game.game.spells.cell_effects.Glyph;
import org.d2j.game.game.spells.cell_effects.Trap;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 18/11/11
 * Time: 17:21
 * IDE : IntelliJ IDEA
 */
public interface IFightHandler {
    void notifyFightJoin(Team challengers, Team defenders) throws FightException;
    void notifyFighterReady(IFighter fighter) throws FightException;
    void notifyAddFighter(IFighter fighter) throws FightException;
    void notifyRemoveFighter(long fighterId) throws FightException;
    void notifyAddFighters(Collection<IFighter> fighters) throws FightException;
    void notifyFighterPlacement(IFighter fighter) throws FightException;
    void notifyFighterQuit(IFighter fighter) throws FightException;
    void notifyQuit() throws FightException;
    void notifyFightStart(Collection<Turn> turns, Collection<IFighter> fighters) throws FightException;
    void notifyTurnStart(Turn turn) throws FightException;
    void notifyTurnStop(Turn turn) throws FightException;
    void notifyFighterMovement(FightMovement movement) throws FightException;
    void notifyBasicAction(ActionTypeEnum actionType, IFighter fighter, int arg) throws FightException;
    void notifyBasicAction(ActionTypeEnum actionType, IFighter fighter, int arg1, int arg2) throws FightException;
    void notifyEndAction(EndActionTypeEnum action, IFighter fighter) throws FightException;
    void notifyRefreshStatistics() throws FightException;
    void notifyFightersInformations(Collection<IFighter> fighters) throws FightException;
    void notifyStartAction(long fighterId) throws FightException;
    void notifyCastSpell(IFighter fighter, ISpell spell, FightCell target) throws FightException;
    void notifyUseWeapon(IFighter fighter, FightCell target) throws FightException;
    void notifyTeleportation(IFighter fighter, FightCell target) throws FightException;
    void notifySlide(IFighter caster, IFighter target) throws FightException;
    void notifyTransposition(IFighter caster, IFighter target) throws FightException;
    void notifyClearBuffs(IFighter caster, IFighter target) throws FightException;
    void notifyInvisible(IFighter caster, IFighter target, boolean add, int remainingTurns) throws FightException;
    void notifyNewBuff(IFighter fighter, Buff buff) throws FightException;
    void notifyFightEnd(Team winners, Team losers) throws FightException;
    void notifyClientMessage(IFighter fighter, String message);
    void notifyTeamClientMessage(IFighter fighter, String message);
    void notifyUpdateAttribute(FightAttributeType attribute, boolean active);
    void notifyNoAction() throws FightException;
    void notifyTrapAdded(Trap trap) throws FightException;
    void notifyTrapUsed(IFighter trigger, Trap trap) throws FightException;
    void notifyTrapDeleted(Trap trap) throws FightException;
    void notifyGlyphAdded(Glyph glyph) throws FightException;
    void notifyGlyphDeleted(Glyph glyph) throws FightException;
}
