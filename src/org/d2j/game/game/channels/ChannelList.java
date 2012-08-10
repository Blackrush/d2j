package org.d2j.game.game.channels;

import org.d2j.common.client.protocol.enums.ChannelEnum;

import java.util.ArrayList;

/**
 * User: Blackrush
 * Date: 22/12/11
 * Time: 18:49
 * IDE : IntelliJ IDEA
 */
public class ChannelList extends ArrayList<ChannelEnum> {
    public static ChannelList parse(String channels){
        ChannelList list = new ChannelList();
        for (char channel : channels.toCharArray()){
            list.add(ChannelEnum.valueOf(channel));
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(size());
        for (ChannelEnum channel : this) if (channel != null) {
            sb.append(channel.toChar());
        }
        return sb.toString();
    }
}
