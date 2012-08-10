package org.d2j.game.game.fights;

import org.d2j.game.model.SpellTemplate;

import java.util.ArrayList;

/**
 * User: Blackrush
 * Date: 17/12/11
 * Time: 15:01
 * IDE : IntelliJ IDEA
 */
public class FightLog extends ArrayList<FightLog.Cast> {
    public static abstract class Cast {
        private IFighter target;

        public Cast(IFighter target) {
            this.target = target;
        }

        public IFighter getTarget(){
            return target;
        }
    }

    public static class SpellCast extends Cast {
        private SpellTemplate spell;

        public SpellCast(IFighter target, SpellTemplate spell) {
            super(target);
            this.spell = spell;
        }

        public SpellTemplate getSpell(){
            return spell;
        }
    }

    public static class WeaponCast extends Cast {
        private int weaponId;

        public WeaponCast(IFighter target, int weaponId) {
            super(target);
            this.weaponId = weaponId;
        }

        int getWeaponId(){
            return weaponId;
        }
    }

    public FightLog() {
    }

    public FightLog castByTarget(IFighter fighter){
        FightLog result = new FightLog();
        for (Cast cast : this){
            if (cast.getTarget() == fighter){
                result.add(cast);
            }
        }
        return result;
    }

    public FightLog castBySpell(SpellTemplate spell){
        FightLog result = new FightLog();
        for (Cast cast : this){
            if (cast instanceof SpellCast && ((SpellCast)cast).getSpell() == spell){
                result.add(cast);
            }
        }
        return result;
    }

    public FightLog castByWeapon(int weaponId){
        FightLog result = new FightLog();
        for (Cast cast : this){
            if (cast instanceof WeaponCast && ((WeaponCast)cast).getWeaponId() == weaponId){
                result.add(cast);
            }
        }
        return result;
    }
}
