package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.service.protocol.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 25/01/12
 * Time: 18:56
 * To change this template use File | Settings | File Templates.
 */
public class ManyCharactersListMessage extends Message {
    public static final int MESSAGE_ID = 14;

    private Map<Integer, Integer> characters;

    public ManyCharactersListMessage() {
    }

    public ManyCharactersListMessage(Map<Integer, Integer> characters) {
        this.characters = characters;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buf) {
        buf.putInt(characters.size());
        for (Map.Entry<Integer, Integer> entry : characters.entrySet()){
            buf.putInt(entry.getKey());
            buf.putInt(entry.getValue());
        }
    }

    @Override
    public void deserialize(IoBuffer buf) {
        int size = buf.getInt();
        characters = new HashMap<>(size);
        for (int i = 0; i < size; ++i){
            characters.put(buf.getInt(), buf.getInt());
        }
    }

    public Map<Integer, Integer> getCharacters() {
        return characters;
    }

    public void setCharacters(Map<Integer, Integer> characters) {
        this.characters = characters;
    }
}
