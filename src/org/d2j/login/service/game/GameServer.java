package org.d2j.login.service.game;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.common.client.protocol.type.GameServerType;
import org.d2j.common.service.protocol.messages.ClientConnectionMessage;
import org.d2j.login.model.LoginAccount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 10:29
 * IDE : IntelliJ IDEA
 */
public class GameServer {
    public static List<GameServerType> toGameServerType(Collection<GameServer> servers){
        List<GameServerType> types = new ArrayList<>();
        for (GameServer gs : servers){
            types.add(gs.toGameServerType());
        }

        return types;
    }

    private IoSession session;
    private boolean sync;

    private int id;
    private int remotePort;
    private String remoteAddress;
    private WorldStateEnum state;
    private int completion;

    public GameServer(IoSession session) {
        this.session = session;
    }

    public IoSession getSession() {
        return session;
    }

    public void setSession(IoSession session) {
        this.session = session;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public GameServerType toGameServerType(){
        return new GameServerType(id, remoteAddress, remotePort, state, completion);
    }

    public WriteFuture sendAccountInformations(String ticket, LoginAccount account){
        return session.write(new ClientConnectionMessage(
                account.getId(),
                ticket,
                account.getNickname(),
                account.getAnswer(),
                account.getRights(),
                account.getCommunity()
        ));
    }
}
