package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.Permissions;
import org.d2j.common.service.protocol.Message;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 02/02/12
 * Time: 18:26
 * To change this template use File | Settings | File Templates.
 */
public class AccountUpdateMessage extends Message {
    public static final int MESSAGE_ID = 15;

    private int accountId;
    private Permissions permissions;

    public AccountUpdateMessage() {
    }

    public AccountUpdateMessage(int accountId, Permissions permissions) {
        this.accountId = accountId;
        this.permissions = permissions;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(accountId);
        buffer.putEnum(permissions);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        accountId = buffer.getInt();
        permissions = buffer.getEnum(Permissions.class);
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
