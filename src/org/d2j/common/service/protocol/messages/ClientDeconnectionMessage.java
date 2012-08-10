package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.service.protocol.Message;

/**
 * User: Blackrush
 * Date: 11/11/11
 * Time: 17:35
 * IDE : IntelliJ IDEA
 */
public class ClientDeconnectionMessage extends Message {
    public static final int MESSAGE_ID = 13;

    private int accountId;

    public ClientDeconnectionMessage() {

    }

    public ClientDeconnectionMessage(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(accountId);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        accountId = buffer.getInt();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}
