package org.d2j.game.game;

import org.d2j.game.model.Item;
import org.d2j.game.model.Spell;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Blackrush
 * Date: 03/12/11
 * Time: 17:47
 * IDE : IntelliJ IDEA
 */
public class QuickBar {
    private final Character owner;

    private final List<Spell> spells = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();

    public QuickBar(Character owner) {
        this.owner = owner;
    }

    public Character getOwner() {
        return owner;
    }

    public List<Spell> getSpells() {
        return spells;
    }

    public boolean add(Spell spell){
        return spells.add(spell);
    }

    public boolean remove(Spell spell){
        return spells.remove(spell);
    }

    public boolean contains(Spell spell){
        return spells.contains(spell);
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean add(Item item){
        return items.add(item);
    }

    public boolean remove(Item item){
        return items.remove(item);
    }

    public boolean contains(Item item){
        return items.contains(item);
    }
}
