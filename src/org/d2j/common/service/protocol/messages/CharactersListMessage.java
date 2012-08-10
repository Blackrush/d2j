package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.MessageFactory;
import org.d2j.common.service.protocol.MessageMaker;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 11:59
 * IDE : IntelliJ IDEA
 */
public class CharactersListMessage extends Message {
    public static final int MESSAGE_ID = 4;

    private int accountId;
    private int charactersList;

    public CharactersListMessage() {

    }

    public CharactersListMessage(int accountId, int charactersList) {
        this.accountId = accountId;
        this.charactersList = charactersList;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(accountId);
        buffer.putInt(charactersList);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        accountId = buffer.getInt();
        charactersList = buffer.getInt();
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCharactersList() {
        return charactersList;
    }

    public void setCharactersList(int charactersList) {
        this.charactersList = charactersList;
    }
}
