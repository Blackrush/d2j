package org.d2j.game.game.statistics;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:51
 * IDE : IntelliJ IDEA
 */
public class Characteristic implements ICharacteristic {
    private short base;
    private short equipments;
    private short gifts;
    private short context;

    public Characteristic() {

    }

    public Characteristic(short base, short equipments, short gifts, short context) {
        this.base = base;
        this.equipments = equipments;
        this.gifts = gifts;
        this.context = context;
    }

    public short getBase() {
        return base;
    }

    public void setBase(short base) {
        this.base = base;
    }

    @Override
    public void addBase(short base) {
        this.base += base;
    }

    public short getEquipments() {
        return equipments;
    }

    public void setEquipments(short equipments) {
        this.equipments = equipments;
    }

    @Override
    public void addEquipments(short equipments) {
        this.equipments += equipments;
    }

    public short getGifts() {
        return gifts;
    }

    public void setGifts(short gifts) {
        this.gifts = gifts;
    }

    @Override
    public void addGifts(short gifts) {
        this.gifts += gifts;
    }

    public short getContext() {
        return context;
    }

    public void setContext(short context) {
        this.context = context;
    }

    @Override
    public void addContext(short context) {
        this.context += context;
    }

    @Override
    public short getTotal() {
        return (short) (base + equipments + gifts + context);
    }

    @Override
    public short getSafeTotal() {
        short total = getTotal();
        return total >= 0 ? total : 0;
    }

    @Override
    public void reset() {
        base = 0;
        equipments = 0;
        gifts = 0;
        context = 0;
    }

    @Override
    public ICharacteristic copy(IStatistics statistics) {
        return new Characteristic(base, equipments, gifts, context);
    }
}
