package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.cell_effects.BaseTrap;
import org.d2j.game.game.spells.cell_effects.Trap;
import org.d2j.game.game.spells.zones.SingleCell;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.LazyLoadEntity;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 17:10
 */
public class UseTrapEffect extends Effect {
    private LazyLoadEntity<SpellTemplate, Integer> spell;
    private short spellLevel;
    private Zone zone;

    public UseTrapEffect(ISpellLevel infos, IBaseEntityRepository<SpellTemplate, Integer> spells) {
        super(infos, SpellEffectsEnum.UseTrap);
        this.spell = new LazyLoadEntity<>(spells);
    }

    @Override
    public int getNbTurns() {
        return 0;
    }

    @Override
    public Zone getZone() {
        return SingleCell.INSTANCE;
    }

    @Override
    public void setZone(Zone zone) {
        this.zone = zone;
        this.zone.setSize(this.zone.getSize() + 1);
    }

    @Override
    public void apply(AppendableFightHandlerAction action, IFighter caster, FightCell target) throws FightException {
        Fight fight = target.getFight();

        final Trap trap = new BaseTrap(
                fight,
                caster,
                infos.getTemplate(),
                target,
                spell.get().getLevels().get(spellLevel),
                zone
        );

        trap.add();

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyTrapAdded(trap);
            }
        });
    }

    @Override
    public int getValue1() {
        return !spell.isNull() ? spell.get().getId() : 0;
    }

    @Override
    public void setValue1(int value1) {
        this.spell.setId(value1);
    }

    @Override
    public int getValue2() {
        return this.spellLevel;
    }

    @Override
    public void setValue2(int value2) {
        this.spellLevel = (short) value2;
    }

    @Override
    public int getValue3() {
        return 0;
    }

    @Override
    public void setValue3(int value3) {
    }

    @Override
    public int getChance() {
        return 0;
    }

    @Override
    public void setChance(int chance) {
    }

    @Override
    public Dice getDice() {
        return null;
    }

    @Override
    public void setDice(Dice dice) {
    }
}
