package org.d2j.common.client.protocol;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.FightAttributeType;
import org.d2j.common.client.protocol.enums.InfoTypeEnum;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: Blackrush
 * Date: 06/11/11
 * Time: 10:22
 * IDE:  IntelliJ IDEA
 */
public class InfoGameMessageFormatter {
    public static final SimpleDateFormat LAST_CONNECTION_FORMATTER = new SimpleDateFormat(
            "yyyy~MM~dd~HH~mm"
    );

    public static String welcomeMessage(){
        return "Im189";
    }

    public static String currentAddressInformationMessage(String currentAddress){
        return "Im153;" + currentAddress;
    }

    public static String lastConnectionInformationMessage(Date lastConnection, String lastAddress){
        return "Im0152;" + LAST_CONNECTION_FORMATTER.format(lastConnection) + "~" + lastAddress;
    }

    public static String accountMutedMessage(){
        return "Im1124;";
    }

    public static String friendConnectedMessage(String friendNickname, String friendCharacterName){
        return StringUtils.format("Im0143;{0} ({1})", friendNickname, urlize(friendCharacterName));
    }

    public static String urlize(String title){
        return StringUtils.format("<b><a href='asfunction:onHref,ShowPlayerPopupMenu,{0}'>{0}</a></b>", title);
    }

    public static String floodMessage(int remaining){
        return "Im0115;" + remaining;
    }

    public static String youReAwayMessage(){
        return "Im072";
    }

    public static String startSaveMessage(){
        return "Im1164";
    }

    public static String endSaveMessage() {
        return "Im1165";
    }

    public static String cantWearItemMessage(){
        return "Im119|43";
    }

    public static String fullGuildMessage(int availablePlaces){
        return "Im155;" + availablePlaces;
    }

    public static String fightAttributeActivationMessage(FightAttributeType attribute, boolean active){
        switch (attribute) {
            case DENY_ALL:
                if (active) return "Im095";
                return "Im096";

            case NEED_HELP:
                if (active) return "Im103";
                return "Im104";

            case ALLOW_PARTY:
                if (active) return "Im093";
                return "Im094";

            case DENY_SPECTATORS:
                if (active) return "Im040";
                return "Im039";

            default:
                return null;
        }
    }

    public static String newZaapMessage(){
        return "Im024";
    }

    public static String notEnoughStorePlacesMessage(byte maxStores){
        return "Im125;" + maxStores;
    }

    public static String emptyStoreMessage(){
        return "Im123";
    }

    public static String waypointSavedMessage(){
        return "Im06";
    }

    public static String notEnoughKamasMessage(){
        return "Im063";
    }

    public static String genericInfoMessage(InfoTypeEnum type){
        return "Im" + type.value();
    }
}
