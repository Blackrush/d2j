package org.d2j.game.model;

import org.d2j.game.game.spells.ISpellLevel;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.HashMap;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 16:59
 * IDE : IntelliJ IDEA
 */
public class SpellTemplate implements IBaseEntity<Integer> {
    private int id;
    private String name;
    private int sprite;
    private String spriteInfos;

    private HashMap<Short, ISpellLevel> levels = new HashMap<>();

    public SpellTemplate(int id, String name, int sprite, String spriteInfos) {
        this.id = id;
        this.name = name;
        this.sprite = sprite;
        this.spriteInfos = spriteInfos;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getSprite() {
        return sprite;
    }

    public String getSpriteInfos() {
        return spriteInfos;
    }

    public HashMap<Short, ISpellLevel> getLevels() {
        return levels;
    }

    public Spell newInstance(){
        Spell spell = new Spell();
        spell.setTemplate(this);
        return spell;
    }
}
