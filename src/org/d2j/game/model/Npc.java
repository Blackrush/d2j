package org.d2j.game.model;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.OrientationEnum;
import org.d2j.common.client.protocol.type.RolePlayNpcType;
import org.d2j.common.client.protocol.type.BaseRolePlayActorType;
import org.d2j.game.game.RolePlayActor;
import org.d2j.utils.database.entity.IEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/01/12
 * Time: 19:15
 * To change this template use File | Settings | File Templates.
 */
public class Npc implements IEntity<Integer>, RolePlayActor {
    private int id;
    private long publicId;
    private NpcTemplate template;
    private Map map;
    private short cellId;
    private OrientationEnum orientation;

    public Npc(int id, NpcTemplate template, Map map, short cellId, OrientationEnum orientation) {
        this.id = id;
        this.template = template;
        this.map = map;
        this.cellId = cellId;
        this.orientation = orientation;
    }

    public Npc(NpcTemplate template, Map map, short cellId, OrientationEnum orientation) {
        this.template = template;
        this.map = map;
        this.cellId = cellId;
        this.orientation = orientation;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public long getPublicId() {
        return publicId;
    }

    public void setPublicId(long publicId) {
        this.publicId = publicId;
    }

    public NpcTemplate getTemplate() {
        return template;
    }

    public Map getMap() {
        return map;
    }

    @Override
    public short getCurrentCell() {
        return cellId;
    }

    @Override
    public OrientationEnum getCurrentOrientation() {
        return orientation;
    }

    @Override
    public BaseRolePlayActorType toBaseRolePlayActorType() {
        return new RolePlayNpcType(
                publicId,
                template.getId(),
                template.getScaleX(),
                template.getScaleY(),
                template.getSkin(),
                template.getGender(),
                template.getColor1(),
                template.getColor2(),
                template.getColor3(),
                cellId,
                orientation,
                template.getAccessories(),
                template.getCustomArtwork(),
                template.getExtraClip()
        );
    }

    @Override
    public void beforeSave() {
    }

    @Override
    public void onSaved() {
    }

    @Override
    public String toString() {
        return StringUtils.format("{id: {0}, publicId: {1}}", id, publicId);
    }
}
