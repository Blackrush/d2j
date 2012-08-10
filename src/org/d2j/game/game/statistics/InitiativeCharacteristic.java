package org.d2j.game.game.statistics;

/**
 * User: Blackrush
 * Date: 12/11/11
 * Time: 10:58
 * IDE : IntelliJ IDEA
 */
public class InitiativeCharacteristic extends Characteristic {
    private final IStatistics statistics;

    public InitiativeCharacteristic(IStatistics statistics) {
        super();

        this.statistics = statistics;
    }

    public InitiativeCharacteristic(IStatistics statistics, short base, short equipments, short gifts, short context) {
        super(base, equipments, gifts, context);

        this.statistics = statistics;
    }

    @Override
    public short getTotal() {
        return (short)((statistics.get(CharacteristicType.Strength).getTotal() +
                        statistics.get(CharacteristicType.Intelligence).getTotal() +
                        statistics.get(CharacteristicType.Chance).getTotal() +
                        statistics.get(CharacteristicType.Agility).getTotal())
                        *
                       (statistics.getLife() / statistics.getMaxLife()));
    }

    @Override
    public ICharacteristic copy(IStatistics statistics) {
        return new InitiativeCharacteristic(statistics, getBase(), getEquipments(), getGifts(), getContext());
    }
}
