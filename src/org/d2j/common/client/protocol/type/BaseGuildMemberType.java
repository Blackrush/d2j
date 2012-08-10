package org.d2j.common.client.protocol.type;

import org.joda.time.Instant;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 04/02/12
 * Time: 21:11
 * To change this template use File | Settings | File Templates.
 */
public class BaseGuildMemberType {
    private long id;
    private String name;
    private short level;
    private short skin;
    private int rank;
    private byte experienceGiven;
    private long experienceRate;
    private int rights;
    private boolean online;
    private int alignment;
    private Instant lastConnection;

    public BaseGuildMemberType() {
    }

    public BaseGuildMemberType(long id, String name, short level, short skin, int rank, byte experienceGiven, long experienceRate, int rights, boolean online, int alignment, Instant lastConnection) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.skin = skin;
        this.rank = rank;
        this.experienceGiven = experienceGiven;
        this.experienceRate = experienceRate;
        this.rights = rights;
        this.online = online;
        this.alignment = alignment;
        this.lastConnection = lastConnection;
    }

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

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public short getSkin() {
        return skin;
    }

    public void setSkin(short skin) {
        this.skin = skin;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public byte getExperienceGiven() {
        return experienceGiven;
    }

    public void setExperienceGiven(byte experienceGiven) {
        this.experienceGiven = experienceGiven;
    }

    public long getExperienceRate() {
        return experienceRate;
    }

    public void setExperienceRate(long experienceRate) {
        this.experienceRate = experienceRate;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public Instant getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(Instant lastConnection) {
        this.lastConnection = lastConnection;
    }
}
