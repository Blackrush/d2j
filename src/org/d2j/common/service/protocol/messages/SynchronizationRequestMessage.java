package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.common.service.protocol.MessageMaker;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 10:37
 * IDE : IntelliJ IDEA
 */
public class SynchronizationRequestMessage extends Message {
    public static final int MESSAGE_ID = 11;

    private int serverId;

    public SynchronizationRequestMessage() {
    }

    public SynchronizationRequestMessage(int serverId) {
        this.serverId = serverId;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(serverId);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        serverId = buffer.getInt();
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }
}
