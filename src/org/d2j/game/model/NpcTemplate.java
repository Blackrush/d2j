package org.d2j.game.model;

import org.d2j.common.client.protocol.enums.NpcTypeEnum;
import org.d2j.game.game.npcs.SellList;
import org.d2j.utils.database.entity.IBaseEntity;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/01/12
 * Time: 18:34
 * To change this template use File | Settings | File Templates.
 */
public class NpcTemplate implements IBaseEntity<Integer> {
    private int id;
    private NpcTypeEnum type;
    private short skin;
    private short scaleX, scaleY;
    private boolean gender;
    private int color1, color2, color3;
    private NpcQuestion question;
    private Collection<Integer> accessories;
    private int extraClip, customArtwork;
    private SellList sells;

    public NpcTemplate(int id, NpcTypeEnum type, short skin, short scaleX, short scaleY, boolean gender, int color1, int color2, int color3, NpcQuestion question, Collection<Integer> accessories, int extraClip, int customArtwork) {
        this.id = id;
        this.type = type;
        this.skin = skin;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.gender = gender;
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.question = question;
        this.accessories = accessories;
        this.extraClip = extraClip;
        this.customArtwork = customArtwork;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public NpcTypeEnum getType() {
        return type;
    }

    public short getSkin() {
        return skin;
    }

    public short getScaleX() {
        return scaleX;
    }

    public short getScaleY() {
        return scaleY;
    }

    public boolean getGender() {
        return gender;
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    public int getColor3() {
        return color3;
    }

    public NpcQuestion getQuestion() {
        return question;
    }

    public Collection<Integer> getAccessories() {
        return accessories;
    }

    public int getExtraClip() {
        return extraClip;
    }

    public int getCustomArtwork() {
        return customArtwork;
    }

    public SellList getSells(){
        return sells;
    }

    public void setSells(SellList sells) {
        this.sells = sells;
    }
}
