package org.d2j.common.client.protocol;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.ItemPositionEnum;
import org.d2j.common.client.protocol.type.BaseItemEffectType;
import org.d2j.common.client.protocol.type.BaseItemType;

import java.util.Collection;

import static org.d2j.common.StringUtils.toHex;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:15
 * IDE:  IntelliJ IDEA
 */
public class ItemGameMessageFormatter {
    public static void parseAccessories(StringBuilder sb, int[] accessories){
        boolean first = true;
        for (int accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(accessory > 0 ? StringUtils.toHex(accessory) : "");
        }
    }

    public static void parseAccessories(StringBuilder sb, Iterable<Integer> accessories){
        boolean first = true;
        for (Integer accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(accessory != null && accessory > 0 ? StringUtils.toHex(accessory) : "");
        }
    }

    public static String inventoryStatsMessage(int usedPods, int maxPods){
        return "Ow" + usedPods + "|" + maxPods;
    }

    private static void formatItem(StringBuilder sb, BaseItemType item){
        sb.append(toHex(item.getId())).append('~');
        sb.append(toHex(item.getTemplateId())).append('~');
        sb.append(toHex(item.getQuantity())).append('~');
        sb.append(item.getPosition() != ItemPositionEnum.NotEquiped ? toHex(item.getPosition().value()) : "").append('~');

        boolean first = true;
        for (BaseItemEffectType effect : item.getEffects()){
            if (first) first = false;
            else sb.append(',');

            sb.append(toHex(effect.getEffect().value())).append('#');
            sb.append(toHex(effect.getBonus())).append('#');
            sb.append("0#");
            sb.append("0#");
            sb.append("0d+0");
        }
    }

    public static void formatItems(StringBuilder sb, Collection<BaseItemType> items){
        boolean first = true;
        for (BaseItemType item : items){
            if (first) first = false;
            else sb.append(';');

            formatItem(sb, item);
        }
    }

    public static String addItemMessage(BaseItemType item){
        StringBuilder sb = new StringBuilder().append("OAKO");
        formatItem(sb, item);
        return sb.toString();
    }

    public static String quantityMessage(long itemId, int quantity) {
        return "OQ" + itemId + "|" + quantity;
    }

    public static String deleteMessage(long itemId) {
        return "OR" + itemId;
    }

    public static String itemMovementMessage(long itemId, ItemPositionEnum position){
        return "OM" + itemId + "|" + (position != ItemPositionEnum.NotEquiped ? position.value() : "");
    }

    public static String alreadyEquipedMessage() {
        return "OMEA";
    }

    public static String tooSlowLevelMessage() {
        return "OMEL";
    }

    public static String fullInventoryMessage() {
        return "OMEF";
    }

    public static String accessoriesMessage(long playerId, int[] accessories){
        StringBuilder sb = new StringBuilder().append("Oa");

        sb.append(playerId).append('|');

        boolean first = true;
        for (int accessory : accessories){
            if (first) first = false;
            else sb.append(',');

            sb.append(accessory != -1 ? toHex(accessory) : "");
        }

        return sb.toString();
    }

    public static String addItemSetMessage(short itemSetId, Collection<BaseItemType> items, Collection<BaseItemEffectType> effects) {
        StringBuilder sb = new StringBuilder().append("OS+");

        sb.append(itemSetId).append('|');

        boolean first = true;
        for (BaseItemType item : items){
            if (first) first = false;
            else sb.append(';');

            sb.append(item.getId());
        }
        sb.append('|');

        first = true;
        for (BaseItemEffectType effect : effects){
            if (first) first = false;
            else sb.append(',');

            sb.append(toHex(effect.getEffect().value())).append('#');
            sb.append(toHex(effect.getBonus())).append('#');
            sb.append("0#");
            sb.append("0");
        }

        return sb.toString();
    }

    public static String removeItemSetMessage(short itemSetId) {
        return "OS-" + itemSetId;
    }
}
