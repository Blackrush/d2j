package org.d2j.game.game.fights;

import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.IStatistics;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Blackrush
 * Date: 21/12/11
 * Time: 15:10
 * IDE : IntelliJ IDEA
 */
public class FightUtils {
    private static AtomicReference<Random> RANDOM = new AtomicReference<Random>(new Random(System.nanoTime()));

    public static boolean computeFailure(short criticalFailureRate, IFighter caster) {
        criticalFailureRate += caster.getStatistics().get(CharacteristicType.CriticalFailure).getTotal();
        if (criticalFailureRate < 2){
            criticalFailureRate = 2;
        }
        return RANDOM.get().nextInt(criticalFailureRate) == 1;
    }

    public static boolean computeCritical(short criticalRate, IFighter caster) {
        short agility = caster.getStatistics().get(CharacteristicType.Agility).getSafeTotal();
        criticalRate -= caster.getStatistics().get(CharacteristicType.CriticalHit).getSafeTotal();
        criticalRate = (short)((criticalRate * 2.9901) / Math.log(agility + 12));

        if (criticalRate < 2){
            criticalRate = 2;
        }
        return RANDOM.get().nextInt(criticalRate) == 1;
    }

    public static boolean computeTackle(IStatistics caster, IStatistics target){
        double cAgi = (double)caster.get(CharacteristicType.Agility).getSafeTotal(),
               tAgi = (double)target.get(CharacteristicType.Agility).getSafeTotal();

        double agi1 = (cAgi + 25) * (cAgi + 25),
               agi2 = agi1 + (tAgi + 25) * (tAgi + 25);

        int percent = (int) ((agi1 / agi1) * 100);

        return RANDOM.get().nextInt(100) <= percent;
    }

    public static FightTeamEnum getWinnerTeam(Team team1, Team team2) {
        int nbSurvivors1 = 0,
            nbSurvivors2 = 0;

        for (IFighter fighter : team1.getFighters()){
            if (fighter.isAlive()){
                ++nbSurvivors1;
            }
        }

        for (IFighter fighter : team2.getFighters()){
            if (fighter.isAlive()){
                ++nbSurvivors1;
            }
        }

        if (nbSurvivors1 < nbSurvivors2) {
            return team2.getTeamType();
        }
        else {
            return team1.getTeamType();
        }
    }

    public static FightTeamEnum oppositeTeam(FightTeamEnum team){
        switch (team){
            case CHALLENGER:
                return FightTeamEnum.DEFENDER;
            case DEFENDER:
                return FightTeamEnum.CHALLENGER;
            default:
                return null;
        }
    }
}
