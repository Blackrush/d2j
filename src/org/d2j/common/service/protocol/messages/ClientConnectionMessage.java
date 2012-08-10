package org.d2j.common.service.protocol.messages;

import org.apache.mina.core.buffer.IoBuffer;
import org.d2j.common.Permissions;
import org.d2j.common.StringUtils;
import org.d2j.common.service.protocol.Message;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 13:32
 * IDE : IntelliJ IDEA
 */
public class ClientConnectionMessage extends Message {
    public static final int MESSAGE_ID = 5;

    private Integer accountId;
    private String ticket;
    private String nickname;
    private String answer;
    private Permissions rights;
    private int community;

    public ClientConnectionMessage() {

    }

    public ClientConnectionMessage(Integer accountId, String ticket, String nickname, String answer, Permissions rights, int community) {
        this.accountId = accountId;
        this.ticket = ticket;
        this.nickname = nickname;
        this.answer = answer;
        this.rights = rights;
        this.community = community;
    }

    @Override
    public int getMessageId() {
        return MESSAGE_ID;
    }

    @Override
    public void serialize(IoBuffer buffer) {
        buffer.putInt(accountId);
        StringUtils.putString(buffer, ticket);
        StringUtils.putString(buffer, nickname);
        StringUtils.putString(buffer, answer);
        buffer.putEnum(rights);
        buffer.putInt(community);
    }

    @Override
    public void deserialize(IoBuffer buffer) {
        accountId = buffer.getInt();
        ticket = StringUtils.getString(buffer);
        nickname = StringUtils.getString(buffer);
        answer = StringUtils.getString(buffer);
        rights = buffer.getEnum(Permissions.class);
        community = buffer.getInt();
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Permissions getRights() {
        return rights;
    }

    public void setRights(Permissions rights) {
        this.rights = rights;
    }

    public int getCommunity() {
        return community;
    }

    public void setCommunity(int community) {
        this.community = community;
    }
}
