package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.filters.Filter;
import org.d2j.game.game.spells.zones.Zone;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 20:20
 * IDE : IntelliJ IDEA
 */
public abstract class Effect {
    protected final ISpellLevel infos;
    protected final SpellEffectsEnum effect;

    private int nbTurns;
    private Zone zone;
    private Filter filter;

    protected Effect(ISpellLevel infos, SpellEffectsEnum effect) {
        this.infos = infos;
        this.effect = effect;
    }

    public SpellEffectsEnum getEffect(){
        return effect;
    }

    public int getNbTurns(){
        return nbTurns;
    }

    public void setNbTurns(int nbTurns){
        this.nbTurns = nbTurns;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public abstract void apply(AppendableFightHandlerAction action, IFighter caster, FightCell target) throws FightException;

    public abstract int getValue1();
    public abstract void setValue1(int value1);

    public abstract int getValue2();
    public abstract void setValue2(int value2);

    public abstract int getValue3();
    public abstract void setValue3(int value3);

    public abstract int getChance();
    public abstract void setChance(int chance);

    public abstract Dice getDice();
    public abstract void setDice(Dice dice);
}
