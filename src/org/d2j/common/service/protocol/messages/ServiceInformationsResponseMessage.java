package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.common.service.protocol.Message;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 10:51
 * IDE : IntelliJ IDEA
 */
public class ServiceInformationsResponseMessage extends Message {
    public static final int MESSAGE_ID = 8;

    private int remotePort;
    private String remoteAddress;
    private WorldStateEnum state;
    private int completion;

    public ServiceInformationsResponseMessage() {

    }

    public ServiceInformationsResponseMessage(int remotePort, String remoteAddress, WorldStateEnum state, int completion) {
        this.remotePort = remotePort;
        this.remoteAddress = remoteAddress;
        this.state = state;
        this.completion = completion;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(remotePort);
        StringUtils.putString(buffer, remoteAddress);
        buffer.putEnumInt(state);
        buffer.putInt(completion);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        remotePort = buffer.getInt();
        remoteAddress = StringUtils.getString(buffer);
        state = buffer.getEnumInt(WorldStateEnum.class);
        completion = buffer.getInt();
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
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
