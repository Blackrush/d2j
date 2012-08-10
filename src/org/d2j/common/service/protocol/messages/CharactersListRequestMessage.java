package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.common.service.protocol.MessageMaker;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 11:57
 * IDE : IntelliJ IDEA
 */
public class CharactersListRequestMessage extends Message {
    public static final int MESSAGE_ID = 3;

    private int accountId;

    public CharactersListRequestMessage() {

    }

    public CharactersListRequestMessage(int accountId) {
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
