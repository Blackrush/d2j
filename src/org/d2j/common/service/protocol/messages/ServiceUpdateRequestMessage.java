package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.common.service.protocol.MessageMaker;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 11:45
 * IDE : IntelliJ IDEA
 */
public class ServiceUpdateRequestMessage extends Message {
    public static final int MESSAGE_ID = 9;

    private WorldStateEnum state;
    private int completion;

    public ServiceUpdateRequestMessage() {

    }

    public ServiceUpdateRequestMessage(WorldStateEnum state, int completion) {
        this.state = state;
        this.completion = completion;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(state.ordinal());
        buffer.putInt(completion);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        state = buffer.getEnumInt(WorldStateEnum.class);
        completion = buffer.getInt();
    }

    public WorldStateEnum getState() {
        return state;
    }

    public void setState(WorldStateEnum state) {
        this.state = state;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }
}
