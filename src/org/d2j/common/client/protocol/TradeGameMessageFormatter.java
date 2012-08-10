package org.d2j.common.client.protocol;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.TradeErrorEnum;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.common.client.protocol.type.*;

import java.util.Collection;

import static org.d2j.common.StringUtils.toHex;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 13/01/12
 * Time: 19:19
 * To change this template use File | Settings | File Templates.
 */
public class TradeGameMessageFormatter {
    public static String startTradeMessage(TradeTypeEnum type){
        return "ECK" + type.value();
    }

    public static String startTradeMessage(TradeTypeEnum type, long traderId){
        return "ECK" + type.value() + "|" + traderId;
    }

    public static String tradeRequestErrorMessage(TradeErrorEnum error){
        return "ERE" + error.value();
    }

    public static String tradeRequestMessage(long sourceId, long targetId, TradeTypeEnum type){
        return "ERK" + sourceId + "|" + targetId + "|" + type.value();
    }

    public static String tradeQuitMessage(){
        return "EV";
    }

    public static String tradeLocalSetKamasMessage(long kamas) {
        return "EMKG" + kamas;
    }

    public static String tradeRemoteSetKamasMessage(long kamas) {
        return "EmKG" + kamas;
    }

    public static String tradeReadyMessage(boolean ready, long sourceId){
        return "EK" + (ready ? "1" : "0") + sourceId;
    }

    public static String tradeLocalPutItemMessage(long itemId, int quantity){
        return "EMKO+" + itemId + "|" + quantity;
    }

    public static String tradeRemotePutItemMessage(BaseItemType item){
        StringBuilder sb = new StringBuilder().append("EmKO+");

        sb.append(item.getId()).append('|');
        sb.append(item.getQuantity()).append('|');
        sb.append(item.getTemplateId()).append('|');

        boolean first = true;
        for (BaseItemEffectType effect : item.getEffects()){
            if (first) first = false;
            else sb.append(',');

            sb.append(toHex(effect.getEffect().value())).append('#');
            sb.append(toHex(effect.getBonus())).append('#');
            sb.append("0#0#0d0+0");
        }

        return sb.toString();
    }

    public static String tradeLocalRemoveItemMessage(long itemId){
        return "EMKO-" + itemId;
    }

    public static String tradeRemoteRemoveItemMessage(long itemId){
        return "EmKO-" + itemId;
    }

    public static String tradeSuccessMessage(){
        return "EVa";
    }

    public static String itemListMessage(Collection<BaseItemTemplateType> items){
        StringBuilder sb = new StringBuilder().append("EL");

        boolean first = true;
        for (BaseItemTemplateType item : items){
            if (first) first = false;
            else sb.append('|');

            sb.append(item.getId()).append(';');

            boolean first2 = true;
            for (BaseItemTemplateEffectType effect : item.getEffects()){
                if (first2) first2 = false;
                else sb.append(',');

                sb.append(effect.getEffect() != null ? toHex(effect.getEffect().value()) : "0").append('#');
                if (effect.getDice().min() == effect.getDice().max()){
                    sb.append(toHex(effect.getDice().max())).append('#');
                    sb.append('#');
                }
                else{
                    sb.append(toHex(effect.getDice().min())).append('#');
                    sb.append(toHex(effect.getDice().max())).append('#');
                }
                sb.append('#');
                sb.append(effect.getDice().toString());
            }
        }

        return sb.toString();
    }

    public static String buySuccessMessage(){
        return "EBK";
    }

    public static String buyErrorMessage() {
        return "EBE";
    }

    public static String sellSuccessMessage(){
        return "ESK";
    }

    public static String sellErrorMessage() {
        return "ESE";
    }

    public static String storedItemsListMessage(Collection<StoreItemType> items){
        StringBuilder sb = new StringBuilder().append("EL");

        boolean first1 = true;
        for (StoreItemType item : items){
            if (first1) first1 = false;
            else sb.append('|');

            sb.append(item.getId()).append(';');
            sb.append(item.getQuantity()).append(';');
            sb.append(item.getTemplateId()).append(';');

            boolean first2 = true;
            for (BaseItemEffectType effect : item.getEffects()){
                if (first2) first2 = false;
                else sb.append(',');

                sb.append(toHex(effect.getEffect().value())).append('#');
                sb.append(toHex(effect.getBonus())).append('#');
                sb.append("0#0#0d0+0");
            }
            sb.append(';');

            sb.append(item.getPrice());
        }

        return sb.toString();
    }
}
