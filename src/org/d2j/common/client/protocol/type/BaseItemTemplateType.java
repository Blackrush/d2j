package org.d2j.common.client.protocol.type;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 18:43
 * To change this template use File | Settings | File Templates.
 */
public class BaseItemTemplateType {
    private int id;
    private Collection<BaseItemTemplateEffectType> effects;

    public BaseItemTemplateType() {
    }

    public BaseItemTemplateType(int id, Collection<BaseItemTemplateEffectType> effects) {
        this.id = id;
        this.effects = effects;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Collection<BaseItemTemplateEffectType> getEffects() {
        return effects;
    }

    public void setEffects(Collection<BaseItemTemplateEffectType> effects) {
        this.effects = effects;
    }
}
