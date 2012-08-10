package org.d2j.game.game.spells.effects;

import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.cell_effects.BaseGlyph;
import org.d2j.game.game.spells.cell_effects.Glyph;
import org.d2j.game.game.spells.zones.SingleCell;
import org.d2j.game.game.spells.zones.Zone;
import org.d2j.game.model.SpellTemplate;
import org.d2j.utils.database.LazyLoadEntity;
import org.d2j.utils.database.repository.IBaseEntityRepository;

/**
 * Created by IntelliJ IDEA.
 * User: Blackrush
 * Date: 08/03/12
 * Time: 19:33
 */
public class UseGlyphEffect extends Effect {
    private LazyLoadEntity<SpellTemplate, Integer> spell;
    private short level;
    private int color;
    private int nbTurns;
    private Zone zone;

    public UseGlyphEffect(ISpellLevel infos, IBaseEntityRepository<SpellTemplate, Integer> spells) {
        super(infos, SpellEffectsEnum.UseGlyph);
        this.spell = new LazyLoadEntity<>(spells);
    }

    @Override
    public int getNbTurns() {
        return 0;
    }

    @Override
    public void setNbTurns(int nbTurns) {
        this.nbTurns = nbTurns;
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

        final Glyph glyph = new BaseGlyph(
                fight,
                caster,
                target,
                infos.getTemplate(),
                spell.get().getLevels().get(level),
                zone,
                nbTurns,
                color
        );

        glyph.add();
        caster.getGlyphes().add(glyph);

        action.append(new FightHandlerAction() {
            @Override
            public void call(IFightHandler obj) throws FightException {
                obj.notifyGlyphAdded(glyph);
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
        return level;
    }

    @Override
    public void setValue2(int value2) {
        this.level = (short) value2;
    }

    @Override
    public int getValue3() {
        return color;
    }

    @Override
    public void setValue3(int value3) {
        this.color = value3;
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
