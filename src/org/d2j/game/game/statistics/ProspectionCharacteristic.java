package org.d2j.game.game.statistics;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:54
 * IDE : IntelliJ IDEA
 */
public class ProspectionCharacteristic extends Characteristic {
    private final IStatistics statistics;

    public ProspectionCharacteristic(IStatistics statistics) {
        super();

        this.statistics = statistics;
    }

    public ProspectionCharacteristic(IStatistics statistics, short base, short equipments, short gifts, short context) {
        super(base, equipments, gifts, context);

        this.statistics = statistics;
    }

    @Override
    public short getTotal() {
        return (short) (super.getTotal() + statistics.get(CharacteristicType.Chance).getTotal() * 10);
    }

    @Override
    public ICharacteristic copy(IStatistics statistics) {
        return new ProspectionCharacteristic(statistics, getBase(), getEquipments(), getGifts(), getContext());
    }
}
