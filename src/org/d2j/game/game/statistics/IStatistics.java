package org.d2j.game.game.statistics;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:43
 * IDE : IntelliJ IDEA
 */
public interface IStatistics {
    ICharacteristic get(CharacteristicType type);

    short getLife();
    void setLife(short life);
    void setLifeByPercent(int percent);
    short addLife(short life);

    short getMaxLife();

    short getUsedPods();
    short getMaxPods();

    IStatistics reset();
    IStatistics resetContext();
    IStatistics resetEquipments();
    IStatistics refresh();

    IStatistics copy();
}
