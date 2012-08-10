package org.d2j.game.game.statistics;

import org.d2j.common.CollectionUtils;
import org.d2j.common.NumUtils;
import org.d2j.common.client.protocol.GameMessageFormatter;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.model.Character;
import org.d2j.game.model.Item;
import org.d2j.game.model.ItemSetTemplate;
import org.d2j.utils.Selector;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 11:04
 * IDE : IntelliJ IDEA
 */
public class CharacterStatistics implements IStatistics {
    public static final short MAX_ENERGY = (short) 10000;

    private final Character character;
    private final Map<CharacteristicType, ICharacteristic> characteristics;

    private short life;

    private CharacterStatistics(Character character, Map<CharacteristicType, ICharacteristic>  characteristics, short life){
        this.character = character;
        this.characteristics = characteristics;
        this.life = life;
    }

    public CharacterStatistics(Character character) {
        this.character = character;
        this.characteristics = new HashMap<>();

        for (CharacteristicType type : CharacteristicType.values()){
            switch (type){
                case Initiative:
                    characteristics.put(type, new InitiativeCharacteristic(this));
                    break;

                case Prospection:
                    characteristics.put(type, new ProspectionCharacteristic(this));
                    break;

                default:
                    characteristics.put(type, new Characteristic());
                    break;
            }
        }
    }

    public CharacterStatistics
            (Character character,
             short life, short actionPoints, short movementPoints,
             short vitality, short wisdom,
             short strength, short intelligence,
             short chance, short agility)
    {
        this.character = character;
        this.characteristics = new HashMap<>();

        this.life = life;

        for (CharacteristicType type : CharacteristicType.values()){
            switch (type){
                case Initiative:
                    characteristics.put(type, new InitiativeCharacteristic(this));
                    break;

                case Prospection:
                    characteristics.put(type, new ProspectionCharacteristic(this));
                    break;

                case ActionPoints:
                    characteristics.put(type, new Characteristic(actionPoints, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case MovementPoints:
                    characteristics.put(type, new Characteristic(movementPoints, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case Vitality:
                    characteristics.put(type, new VitalityCharacteristic(this, vitality));
                    break;

                case Wisdom:
                    characteristics.put(type, new Characteristic(wisdom, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case Strength:
                    characteristics.put(type, new Characteristic(strength, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case Intelligence:
                    characteristics.put(type, new Characteristic(intelligence, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case Chance:
                    characteristics.put(type, new Characteristic(chance, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                case Agility:
                    characteristics.put(type, new Characteristic(agility, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO));
                    break;

                default:
                    characteristics.put(type, new Characteristic());
                    break;
            }
        }
    }

    @Override
    public ICharacteristic get(CharacteristicType type) {
        return characteristics.get(type);
    }

    @Override
    public short getLife() {
        return life;
    }

    @Override
    public void setLife(short life) {
        this.life = life;
    }

    @Override
    public void setLifeByPercent(int percent) {
        if (percent < 0){
            percent = 0;
        }
        if (percent > 100){
            percent = 100;
        }

        short newLife = (short) (getMaxLife() * percent / 100);
        setLife(newLife);
    }

    @Override
    public short addLife(short life) {
        short initial = this.life;

        this.life += life;

        if (this.life > getMaxLife()){
            this.life = getMaxLife();
        }

        if (this.life < 0){
            this.life = 0;
        }

        return (short) (this.life - initial);
    }

    @Override
    public short getMaxLife() {
        return (short) (character.getBreed().getStartLife() +
                        5 * (character.getExperience().getLevel() - 1) +
                        get(CharacteristicType.Vitality).getTotal());
    }

    public void setMaxLife() {
        this.life = getMaxLife();
    }

    @Override
    public short getUsedPods() {
        short used = 0;

        for (Item item : character.getBag().values()) if (!item.isEquiped()) {
            used += item.getTemplate().getWeight() * item.getQuantity();
        }

        return used;
    }

    @Override
    public short getMaxPods() {
        return (short) (1000 + get(CharacteristicType.Strength).getTotal() * 5);
    }

    @Override
    public CharacterStatistics reset() {
        for (ICharacteristic characteristic : characteristics.values()){
            characteristic.reset();
        }
        return this;
    }

    @Override
    public CharacterStatistics resetContext() {
        for (ICharacteristic characteristic : characteristics.values()){
            characteristic.setContext(NumUtils.SHORT_ZERO);
        }
        return this;
    }

    @Override
    public CharacterStatistics resetEquipments() {
        for (ICharacteristic characteristic : characteristics.values()){
            characteristic.setEquipments(NumUtils.SHORT_ZERO);
        }
        return this;
    }

    @Override
    public CharacterStatistics refresh() {
        resetEquipments();
        refreshEquipments();
        refreshItemSets();
        return this;
    }

    @Override
    public CharacterStatistics copy(){
        CharacterStatisticsDelegate delegate = new CharacterStatisticsDelegate();

        Map<CharacteristicType, ICharacteristic> characs = new HashMap<>(characteristics.size());
        for (Map.Entry<CharacteristicType, ICharacteristic> entry : characteristics.entrySet()){
            characs.put(entry.getKey(), entry.getValue().copy(delegate));
        }

        CharacterStatistics statistics = new CharacterStatistics(character, characs, life);
        delegate.setStatistics(statistics);

        return statistics;
    }

    public String getStatisticsMessage(){
        return GameMessageFormatter.statisticsMessage(
                character.getExperience().getExperience(),
                character.getExperience().min(),
                character.getExperience().max(),
                character.getBag().getKamas(),
                character.getStatsPoints(),
                character.getSpellsPoints(),
                0, NumUtils.SHORT_ZERO, NumUtils.SHORT_ZERO, 0, 0, false, //todo alignment
                this.getLife(),
                this.getMaxLife(),
                character.getEnergy(),
                MAX_ENERGY,
                this.get(CharacteristicType.Initiative).getTotal(),
                this.get(CharacteristicType.Prospection).getTotal(),
                this
        );
    }

    public CharacterStatistics refreshEquipments() {
        for (Item item : character.getBag().values()) if (item.isEquiped()) {
            item.apply(this);
        }
        return this;
    }

    public CharacterStatistics refreshItemSets() {
        Collection<Item> equiped = character.getBag().getEquipedItems();
        Collection<ItemSetTemplate> itemSets = CollectionUtils.regroup(
                equiped,
                new Selector<Item, ItemSetTemplate>() {
                    @Override
                    public ItemSetTemplate select(Item o) {
                        return o.getTemplate().getItemSet();
                    }
                }
        );

        for (ItemSetTemplate itemSet : itemSets){
            int n = 0;
            for (Item item : equiped){
                if (item.getTemplate().getItemSet() == itemSet){
                    ++n;
                }
            }

            itemSet.apply(n, this);
        }

        return this;
    }

    public void restat(IGameConfiguration configuration) {
        get(CharacteristicType.Vitality)    .setBase(NumUtils.SHORT_ZERO);
        get(CharacteristicType.Wisdom)      .setBase(NumUtils.SHORT_ZERO);
        get(CharacteristicType.Strength)    .setBase(NumUtils.SHORT_ZERO);
        get(CharacteristicType.Intelligence).setBase(NumUtils.SHORT_ZERO);
        get(CharacteristicType.Chance)      .setBase(NumUtils.SHORT_ZERO);
        get(CharacteristicType.Agility)     .setBase(NumUtils.SHORT_ZERO);

        character.setStatsPoints((short) (character.getExperience().getLevel() * configuration.getStatsPointsPerLevel() - configuration.getStatsPointsPerLevel()));
    }
}
