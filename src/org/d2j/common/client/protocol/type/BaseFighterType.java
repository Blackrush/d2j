package org.d2j.common.client.protocol.type;

import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.game.game.statistics.IStatistics;

/**
 * User: Blackrush
 * Date: 18/11/11
 * Time: 22:19
 * IDE : IntelliJ IDEA
 */
public abstract class BaseFighterType {
    protected long id;
    protected String name;
    protected byte breedId;
    protected short skin, size;
    protected short level;
    protected short currentCellId;
    protected OrientationEnum orientation;
    protected boolean dead;
    protected IStatistics statistics;

    protected BaseFighterType(long id, String name, byte breedId, short skin, short size, short level, short currentCellId, OrientationEnum orientation, boolean dead, IStatistics statistics) {
        this.id = id;
        this.name = name;
        this.breedId = breedId;
        this.skin = skin;
        this.size = size;
        this.level = level;
        this.currentCellId = currentCellId;
        this.orientation = orientation;
        this.dead = dead;
        this.statistics = statistics;
    }

    public abstract void serialize(StringBuilder packet);

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getBreedId() {
        return breedId;
    }

    public void setBreedId(byte breedId) {
        this.breedId = breedId;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public short getSize() {
        return size;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getCurrentCellId() {
        return currentCellId;
    }

    public void setCurrentCellId(short currentCellId) {
        this.currentCellId = currentCellId;
    }

    public OrientationEnum getOrientation() {
        return orientation;
    }

    public void setOrientation(OrientationEnum orientation) {
        this.orientation = orientation;
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public IStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(IStatistics statistics) {
        this.statistics = statistics;
    }
}
