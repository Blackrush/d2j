package org.d2j.common.client.protocol.type;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.fights.FightTeamEnum;
import org.d2j.game.game.statistics.CharacteristicType;
import org.d2j.game.game.statistics.IStatistics;

/**
 * User: Blackrush
 * Date: 18/11/11
 * Time: 22:24
 * IDE : IntelliJ IDEA
 */
public class CharacterFighterType extends BaseFighterType {
    protected boolean gender;
    protected short alignId, alignLevel;
    protected boolean pvpEnabled;
    protected int color1, color2, color3;
    protected int[] accessories;
    protected IStatistics statistics;
    protected FightTeamEnum team;

    public CharacterFighterType(long id, String name, byte breedId, short skin, short size, short level, short currentCellId, OrientationEnum orientation, boolean dead, IStatistics stats, boolean gender, short alignId, short alignLevel, boolean pvpEnabled, int color1, int color2, int color3, int[] accessories, IStatistics statistics, FightTeamEnum team) {
        super(id, name, breedId, skin, size, level, currentCellId, orientation, dead, stats);

        this.gender = gender;
        this.alignId = alignId;
        this.alignLevel = alignLevel;
        this.pvpEnabled = pvpEnabled;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.accessories = accessories;
        this.statistics = stats;
        this.team = team;
    }

    @Override
    public void serialize(StringBuilder sb) {
        sb.append(breedId).append(';');
        sb.append(skin).append('^').append(size).append(';');
        sb.append(gender ? "1;" : "0;");
        sb.append(level).append(';');
        sb.append(alignId).append(",0,").append(pvpEnabled ? alignLevel : 0).append(',').append(id).append(';');
        sb.append(StringUtils.toHexOrNegative(color1)).append(';');
        sb.append(StringUtils.toHexOrNegative(color2)).append(';');
        sb.append(StringUtils.toHexOrNegative(color3)).append(';');

        ItemGameMessageFormatter.parseAccessories(sb, accessories);
        sb.append(';');

        sb.append(statistics.getLife()).append(';');
        sb.append(statistics.get(CharacteristicType.ActionPoints).getSafeTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.MovementPoints).getSafeTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.ResistancePercentNeutral).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.ResistancePercentEarth).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.ResistancePercentFire).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.ResistancePercentWater).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.ResistancePercentWind).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.DodgeActionPoints).getTotal()).append(';');
        sb.append(statistics.get(CharacteristicType.DodgeMovementPoints).getTotal()).append(';');
        sb.append(team.ordinal()).append(';');
        sb.append(';'); // todo mounts
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public short getAlignId() {
        return alignId;
    }

    public void setAlignId(short alignId) {
        this.alignId = alignId;
    }

    public short getAlignLevel() {
        return alignLevel;
    }

    public void setAlignLevel(short alignLevel) {
        this.alignLevel = alignLevel;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public int getColor1() {
        return color1;
    }

    public void setColor1(int color1) {
        this.color1 = color1;
    }

    public int getColor2() {
        return color2;
    }

    public void setColor2(int color2) {
        this.color2 = color2;
    }

    public int getColor3() {
        return color3;
    }

    public void setColor3(int color3) {
        this.color3 = color3;
    }

    public int[] getAccessories() {
        return accessories;
    }

    public void setAccessories(int[] accessories) {
        this.accessories = accessories;
    }

    public IStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(IStatistics statistics) {
        this.statistics = statistics;
    }

    public FightTeamEnum getTeam() {
        return team;
    }

    public void setTeam(FightTeamEnum team) {
        this.team = team;
    }
}
