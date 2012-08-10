package org.d2j.game.game.statistics;

import org.d2j.game.configuration.IGameConfiguration;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/01/12
 * Time: 21:52
 * To change this template use File | Settings | File Templates.
 */
public class CharacterStatisticsDelegate implements IStatistics {
    private CharacterStatistics statistics;

    public CharacterStatisticsDelegate() {
    }

    public CharacterStatisticsDelegate(CharacterStatistics statistics) {
        this.statistics = statistics;
    }

    public CharacterStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(CharacterStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public ICharacteristic get(CharacteristicType type) {
        return statistics.get(type);
    }

    @Override
    public short getLife() {
        return statistics.getLife();
    }

    @Override
    public void setLife(short life) {
        statistics.setLife(life);
    }

    @Override
    public void setLifeByPercent(int percent) {
        statistics.setLifeByPercent(percent);
    }

    @Override
    public short addLife(short life) {
        return statistics.addLife(life);
    }

    @Override
    public short getMaxLife() {
        return statistics.getMaxLife();
    }

    @Override
    public short getUsedPods() {
        return statistics.getUsedPods();
    }

    @Override
    public short getMaxPods() {
        return statistics.getMaxPods();
    }

    @Override
    public CharacterStatistics reset() {
        return statistics.reset();
    }

    @Override
    public CharacterStatistics resetContext() {
        return statistics.resetContext();
    }

    @Override
    public CharacterStatistics resetEquipments() {
        return statistics.resetEquipments();
    }

    @Override
    public CharacterStatistics refresh() {
        return statistics.refresh();
    }

    @Override
    public CharacterStatistics copy() {
        return statistics.copy();
    }

    public String getStatisticsMessage(IGameConfiguration configuration) {
        return statistics.getStatisticsMessage();
    }

    public CharacterStatistics refreshEquipments() {
        return statistics.refreshEquipments();
    }

    public CharacterStatistics refreshItemSets() {
        return statistics.refreshItemSets();
    }
}
