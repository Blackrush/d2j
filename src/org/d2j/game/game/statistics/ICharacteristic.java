package org.d2j.game.game.statistics;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:45
 * IDE : IntelliJ IDEA
 */
public interface ICharacteristic {
    short getBase();
    void setBase(short base);
    void addBase(short context);

    short getEquipments();
    void setEquipments(short equipments);
    void addEquipments(short context);

    short getGifts();
    void setGifts(short gifts);
    void addGifts(short context);

    short getContext();
    void setContext(short context);
    void addContext(short context);

    /**
     * [short.minvalue; short.maxvalue]
     * @return
     */
    short getTotal();

    /**
     * [0; short.maxvalue]
     * @return
     */
    short getSafeTotal();

    void reset();

    ICharacteristic copy(IStatistics statistics);
}
