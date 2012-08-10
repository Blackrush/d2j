package org.d2j.game.model;

import org.d2j.utils.database.entity.IBaseEntity;

/**
 * User: Blackrush
 * Date: 08/12/11
 * Time: 17:26
 * IDE : IntelliJ IDEA
 */
public class SpellBreed implements IBaseEntity<Integer> {
    private int id;
    private BreedTemplate breed;
    private short level;
    private SpellTemplate spell;
    private byte defaultPosition;

    public SpellBreed(int id, BreedTemplate breed, short level, SpellTemplate spell, byte defaultPosition) {
        this.id = id;
        this.breed = breed;
        this.level = level;
        this.spell = spell;
        this.defaultPosition = defaultPosition;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public BreedTemplate getBreed() {
        return breed;
    }

    public short getLevel() {
        return level;
    }

    public SpellTemplate getSpell() {
        return spell;
    }

    public byte getDefaultPosition() {
        return defaultPosition;
    }
}
