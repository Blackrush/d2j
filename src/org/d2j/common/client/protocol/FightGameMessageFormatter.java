package org.d2j.common.client.protocol;

import org.d2j.common.client.protocol.enums.*;
import org.d2j.common.client.protocol.type.BaseEndFighterType;
import org.d2j.common.client.protocol.type.BaseFighterType;
import org.d2j.common.client.protocol.type.BaseRolePlayActorType;
import org.d2j.game.game.fights.FightTeamEnum;
import org.d2j.game.game.statistics.CharacteristicType;

import java.util.Collection;

/**
 * User: Blackrush
 * Date: 16/11/11
 * Time: 18:55
 * IDE : IntelliJ IDEA
 */
public class FightGameMessageFormatter {
    public static String fightJoinErrorMessage(FightJoinErrorEnum error){
        return "GA;903;" + error.toString();
    }

    public static String newFightMessage(FightStateEnum fightState, boolean canCancel, boolean isChallenge, boolean isSpectator, int remainingTime, FightTypeEnum fightType){
        return "GJK" + fightState.ordinal()     + "|" +
                      (canCancel   ? "1" : "0") + "|" +
                      (isChallenge ? "1" : "0") + "|" +
                      (isSpectator ? "1" : "0") + "|" +
                       remainingTime            + "|" +
                       fightType.ordinal();
    }

    public static String startCellsMessage(String challengersPlaces, String defendersPlaces, FightTeamEnum team){
        return "GP" + challengersPlaces + "|" + defendersPlaces + "|" + team.ordinal();
    }

    public static String teamMessage(long leaderId, Collection<BaseFighterType> fighters){
        StringBuilder sb = new StringBuilder(10 * fighters.size()).append("Gt").append(leaderId);

        for (BaseFighterType fighter : fighters){
            sb.append("|+");
            sb.append(fighter.getId()).append(';');
            sb.append(fighter.getName()).append(';');
            sb.append(fighter.getLevel());
        }

        return sb.toString();
    }

    public static String showFighterMessage(BaseFighterType fighter){
        StringBuilder sb = new StringBuilder(50).append("GM");

        formatShowFighter(fighter, sb);
        fighter.serialize(sb);

        return sb.toString();
    }

    public static String showFightersMessage(Collection<BaseFighterType> fighters){
        StringBuilder sb = new StringBuilder(50 * fighters.size()).append("GM");

        for (BaseFighterType fighter : fighters){
            formatShowFighter(fighter, sb);
            fighter.serialize(sb);
        }

        return sb.toString();
    }

    private static void formatShowFighter(BaseFighterType fighter, StringBuilder sb){
        sb.append("|+");
        sb.append(fighter.getCurrentCellId()).append(';');
        sb.append(fighter.getOrientation().ordinal()).append(';');
        sb.append("0;"); // todo unknown packet parameter
        sb.append(fighter.getId()).append(';');
        sb.append(fighter.getName()).append(';');
    }

    public static String addFlagMessage(long fightId, FightTypeEnum fightType, BaseRolePlayActorType challenger, BaseRolePlayActorType defender){
        StringBuilder sb = new StringBuilder().append("Gc+");
        sb.append(fightId).append(';').append(fightType.ordinal()).append('|');

        sb.append(challenger.getId()).append(';')
          .append(challenger.getCurrentCellId()).append(';')
          .append("0;")                 // {player: 0, monster: 1}
          .append("-1").append('|');    // alignment

        sb.append(defender.getId()).append(';')
          .append(defender.getCurrentCellId()).append(';')
          .append("0;")
          .append("-1");

        return sb.toString();
    }

    public static String addFlagMessage(long fightId, FightTypeEnum fightType, BaseRolePlayActorType challengerLeader, Collection<BaseFighterType> challengers, BaseRolePlayActorType defenderLeader, Collection<BaseFighterType> defenders){
        StringBuilder sb = new StringBuilder();
        sb.append(addFlagMessage(fightId, fightType, challengerLeader, defenderLeader));
        sb.append(teamMessage(challengerLeader.getId(), challengers));
        sb.append(teamMessage(defenderLeader.getId(), defenders));
        return sb.toString();
    }

    public static String removeFlagMessage(long fightId){
        return "Gc-" + fightId;
    }

    public static String flagAttributeMessage(boolean active, FightAttributeType attribute, long leaderId) {
        return "Go" + (active ? "+" : "-") +
                      (attribute == FightAttributeType.DENY_ALL ? "A" : attribute.toString()) +
                      leaderId;
    }

    public static String fighterPlacementMessage(long fighterId, short newCellId, OrientationEnum newOrientation){
        return "GIC|" + fighterId + ";" + newCellId + ";" + newOrientation.ordinal();
    }

    public static String fightersPlacementMessage(Collection<BaseFighterType> fighters){
        StringBuilder sb = new StringBuilder(10 * fighters.size()).append("GIC");

        for (BaseFighterType fighter : fighters){
            sb.append('|');

            sb.append(fighter.getId()).append(';');
            sb.append(fighter.getCurrentCellId()).append(';');
            sb.append(fighter.getOrientation().ordinal());
        }

        return sb.toString();
    }

    public static String fighterReadyMessage(long fighterId, boolean ready){
        return "GR" + (ready ? "1" : "0") + fighterId;
    }

    public static String fighterQuitMessage(long fighterId){
        return "GM|-" + fighterId;
    }

    public static String fightStartMessage(){
        return "GS";
    }

    public static String turnListMessage(Collection<Long> fightersId){
        StringBuilder sb = new StringBuilder(3 + 2 * fightersId.size()).append("GTL");

        for (Long fighterId : fightersId){
            sb.append('|');
            sb.append(fighterId);
        }

        return sb.toString();
    }

    public static String fighterInformationsMessage(Collection<BaseFighterType> fighters){
        StringBuilder sb = new StringBuilder(3 + 20 * fighters.size()).append("GTM");

        for (BaseFighterType fighter : fighters){
            sb.append('|');

            sb.append(fighter.getId()).append(';');
            sb.append(fighter.isDead() ? '1' : '0').append(';');

            if (!fighter.isDead()){
                sb.append(fighter.getStatistics().getLife()).append(';');
                sb.append(fighter.getStatistics().get(CharacteristicType.ActionPoints).getSafeTotal()).append(';');
                sb.append(fighter.getStatistics().get(CharacteristicType.MovementPoints).getSafeTotal()).append(';');
                sb.append(fighter.getCurrentCellId()).append(';');
                sb.append(';'); //todo ???
                sb.append(fighter.getStatistics().getMaxLife());
            }
        }

        return sb.toString();
    }

    public static String turnStartMessage(long fighterId, long remainingTime){
        return "GTS" + fighterId + "|" + remainingTime;
    }

    public static String turnEndMessage(long fighterId){
        return "GTF" + fighterId;
    }

    public static String turnReadyMessage(long fighterId){
        return "GTR" + fighterId;
    }

    public static String actionMessage(ActionTypeEnum actionType, long fighterId, Object... args){
        StringBuilder sb = new StringBuilder(15 + 5 * args.length).append("GA;");

        sb.append(actionType.value()).append(';');
        sb.append(fighterId).append(';');

        boolean first = true;
        for (Object arg : args){
            if (first) first = false;
            else sb.append(',');

            sb.append(arg.toString());
        }

        return sb.toString();
    }

    public static String fightActionMessage(ActionTypeEnum actionType, long fighterId, Object... args){
        StringBuilder sb = new StringBuilder(15 + 5 * args.length).append("GA0;");

        sb.append(actionType.value()).append(';');
        sb.append(fighterId).append(';');

        boolean first = true;
        for (Object arg : args){
            if (first) first = false;
            else sb.append(',');

            sb.append(arg.toString());
        }

        return sb.toString();
    }

    public static String fighterMovementMessage(long fighterId, String path){
        return fightActionMessage(ActionTypeEnum.MOVEMENT, fighterId, path);
    }

    public static String endFightActionMessage(EndActionTypeEnum fightAction, long fighterId){
        return "GAF" + fightAction.value() + "|" + fighterId;
    }

    public static String startActionMessage(long fighterId) {
        return "GAS" + fighterId;
    }

    public static String castSpellActionMessage(long fighterId, int spellId, int spellAnimationId, String spellSpriteInfos, int spellLevel, short targetCellId){
        return actionMessage(
                ActionTypeEnum.CAST_SPELL,
                fighterId,
                spellId,
                targetCellId,
                spellAnimationId,
                spellLevel,
                spellSpriteInfos
        );
    }

    public static String fighterLeftMessage(){
        return "GV";
    }

    public static String fighterBuffMessage(long fighterId, SpellEffectsEnum effectId, int value1, int value2, int value3, int chance, int remainingTurn, Integer spellId) {
        return "GIE" + effectId.ordinal() + ";" +
                       fighterId          + ";" +
                       value1             + ";" +
                       value2             + ";" +
                       value3             + ";" +
                       chance             + ";" +
                       remainingTurn      + ";" +
                       spellId;
    }

    public static String fightEndMessage(long fightDuration, boolean aggression, BaseEndFighterType leaderWinner, Collection<BaseEndFighterType> winners, Collection<BaseEndFighterType> losers) {
        StringBuilder sb = new StringBuilder(20 + 30 * winners.size() + 30 * losers.size()).append("GE");

        //GE4166|958|0|2;1424;Leo-mars;181;0;1317997000;1332575192;1355584000         ; ; ; ;; |0;958;Abcdefghijkl;1  ;0;0         ;0         ;110                ; ; ; ; |
        //GE3665|1  |0|2;1   ;sowen   ;200;0;7407232000;7407232000;9223372036854775807; ; ; ;; |0;2  ;Hymeduz     ;200;0;7407232000;7407232000;9223372036854775807; ; ; ;

        sb.append(fightDuration).append('|');
        sb.append(leaderWinner.getId()).append('|');
        sb.append(aggression ? '1' : '0').append('|');

        formatFightEndMessage(sb, winners, aggression, true);
        formatFightEndMessage(sb, losers, aggression, false);

        return sb.toString();
    }

    private static void formatFightEndMessage(StringBuilder sb, Collection<BaseEndFighterType> team, boolean aggression, boolean winner){
        for (BaseEndFighterType fighter : team){
            if (aggression) formatFightAggressionEndMessage(sb, fighter, winner);
            else formatFightNormalEndMessage(sb, fighter, winner);
            sb.append('|');
        }
    }

    private static void formatFightNormalEndMessage(StringBuilder sb, BaseEndFighterType fighter, boolean winner){
        sb.append(winner ? '2' : '0').append(';');
        sb.append(fighter.getId()).append(';');
        sb.append(fighter.getName()).append(';');
        sb.append(fighter.getLevel()).append(';');
        sb.append(fighter.isAlive() ? '0' : '1').append(';');

        sb.append(fighter.getFloorExperienceMin()).append(';');
        sb.append(fighter.getExperience()).append(';');
        sb.append(fighter.getFloorExperienceMax()).append(';');

        sb.append(fighter.getEarnedExperience() > 0 ? fighter.getEarnedExperience() : "").append(';');
        sb.append(fighter.getEarnedGuildExperience() > 0 ? fighter.getEarnedGuildExperience() : "").append(';');
        sb.append(fighter.getEarnedMountExperience() > 0 ? fighter.getEarnedMountExperience() : "").append(';');

        if (winner){
            sb.append(';');//todo won items
            sb.append(fighter.getEarnedKamas() > 0 ? fighter.getEarnedKamas() : "");
        }
    }

    private static void formatFightAggressionEndMessage(StringBuilder sb, BaseEndFighterType fighter, boolean winner){
        //todo
    }

    public static String trapUsedMessage(long triggerId, int originalSpellId, short triggerCellId, long trapOwnerId) {
        return "GA1;306;" + triggerId + ";" + originalSpellId + "," + triggerCellId + ",407,1,1," + trapOwnerId;
    }

    public static String trapDeletedMessage(long trapOwnerId, short trapCellId, int trapSize) {
        return "GA;999;" + trapOwnerId + ";GDZ-" + trapCellId + ";" + trapSize + ";7";
    }

    public static String localTrapDeleteMessage(long trapOwnerId, short trapCellId) {
        return "GA;999;" + trapOwnerId + ";GDC" + trapCellId;
    }

    public static String trapAddedMessage(long trapOwnerId, short trapCellid, int trapSize) {
        return "GA;999;" + trapOwnerId + ";GDZ+" + trapCellid + ";" + trapSize + ";7";
    }

    public static String localTrapAddedMessage(long trapOwnerId, short trapCellId) {
        return "GA;999;" + trapOwnerId + ";GDC" + trapCellId + ";Haaaaaaaaz3005;";
    }

    public static String glyphAddedMessage(long glyphOwnerId, short glyphCellId, int glyphSize, int glyphColor) {
        return "GA;999;" + glyphOwnerId + ";GDZ+" + glyphCellId + ";" + glyphSize + ";" + glyphColor + String.valueOf(0) +
               "GA;999;" + glyphOwnerId + ";GDC" + glyphCellId + ";Haaaaaaaaa3005;";
    }

    public static String glyphDeletedMessage(long glyphOwnerId, short glyphCellId, int glyphSize, int glyphColor) {
        return "GA;999;" + glyphOwnerId + ";GDZ-" + glyphCellId + ";" + glyphSize + ";" + glyphColor + String.valueOf(0) +
               "GA;999;" + glyphOwnerId + "GDC" + glyphCellId;
    }
}
