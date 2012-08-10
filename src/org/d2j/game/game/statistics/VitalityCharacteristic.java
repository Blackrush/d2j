package org.d2j.game.game.statistics;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 27/01/12
 * Time: 21:45
 * To change this template use File | Settings | File Templates.
 */
public class VitalityCharacteristic extends Characteristic {
    private final IStatistics statistics;

    public VitalityCharacteristic(IStatistics statistics, short vitality) {
        this.statistics = statistics;
        super.setBase(vitality);
    }

    @Override
    public void addBase(short base) {
        super.addBase(base);

        statistics.addLife(base);
    }

    @Override
    public void addEquipments(short equipments) {
        super.addEquipments(equipments);

        statistics.addLife(equipments);
    }

    @Override
    public void addGifts(short gifts) {
        super.addGifts(gifts);

        statistics.addLife(gifts);
    }

    @Override
    public void addContext(short context) {
        super.addContext(context);

        statistics.addLife(context);
    }

    @Override
    public ICharacteristic copy(IStatistics statistics) {
        ICharacteristic charac = new VitalityCharacteristic(statistics, getBase());
        charac.setEquipments(getEquipments());
        charac.setGifts(getGifts());
        charac.setContext(getContext());
        return charac;
    }
}
