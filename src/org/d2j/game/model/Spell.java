package org.d2j.game.model;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.ActionTypeEnum;
import org.d2j.common.client.protocol.enums.EndActionTypeEnum;
import org.d2j.common.client.protocol.type.BaseSpellType;
import org.d2j.game.game.fights.*;
import org.d2j.game.game.pathfinding.Pathfinding;
import org.d2j.game.game.spells.ISpell;
import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.game.game.spells.SpellException;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 18:03
 * IDE : IntelliJ IDEA
 */
public class Spell implements IEntity<Long>, ISpell {
    public static Collection<BaseSpellType> toBaseSpellType(Collection<Spell> spells){
        List<BaseSpellType> types = new ArrayList<>();
        for (Spell spell : spells){
            types.add(spell.toBaseSpellType());
        }
        return types;
    }

    private long id;
    private Character character;
    private SpellTemplate template;
    private byte position;
    private short level;

    public Spell() {

    }

    public Spell(int id, Character character, SpellTemplate template, byte position, short level) {
        this.id = id;
        this.character = character;
        this.template = template;
        this.position = position;
        this.level = level;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public SpellTemplate getTemplate() {
        return template;
    }

    public void setTemplate(SpellTemplate template) {
        this.template = template;
    }

    public byte getPosition() {
        return position;
    }

    public void setPosition(byte position) {
        this.position = position;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public void addLevel(short level) {
        this.level += level;
    }

    public ISpellLevel getInfos(){
        return template.getLevels().get(level);
    }

    public BaseSpellType toBaseSpellType(){
        return new BaseSpellType(
                template.getId(),
                level,
                position >= 0 ? String.valueOf(StringUtils.HASH.charAt(position)) : ""
        );
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }
}
