package org.d2j.login.service.login.handler;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.LoginMessageFormatter;
import org.d2j.login.service.game.GameServer;
import org.d2j.login.service.login.LoginClient;
import org.d2j.login.service.login.LoginClientHandler;
import org.d2j.login.service.login.LoginService;

import java.util.Observable;
import java.util.Observer;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 10:51
 * IDE : IntelliJ IDEA
 */
public class ServerScreenHandler extends LoginClientHandler implements Observer {
    public static final long SUBSCRIPTION_DURATION = 3153600000000L;

    protected ServerScreenHandler(LoginService service, LoginClient client) {
        super(service, client);

        service.getGameServerManager().addObserver(this);
    }

    @Override
    public void parse(String packet) throws Exception {
        if (client.getAccount().getRights() == Permissions.BANNED){
            client.getSession().close(true);
            return;
        }

        if (packet.charAt(0) != 'A')
            throw new Exception("Bad data received : " + packet);

        switch (packet.charAt(1)){
            case 'f': //queue
                break;

            case 'x': //characters list
                client.getSession().write(LoginMessageFormatter.charactersListMessage(
                        SUBSCRIPTION_DURATION,
                        client.getAccount().getCharacters()
                ));
                break;

            case 'X': //server selection
                parseServerSelection(Integer.parseInt(packet.substring(2)));
                break;

            default:
                throw new Exception("Bad data received : " + packet);
        }
    }

    @Override
    public void onClosed() {
        service.getGameServerManager().deleteObserver(this);
        client.getAccount().setConnected(false);
    }

    public void update(Observable o, Object arg) {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(LoginMessageFormatter.serversInformationsMessage(
                    GameServer.toGameServerType(service.getGameServerManager().getServers()),
                    true // todo subscription
            ));

            buf.append(LoginMessageFormatter.charactersListMessage(
                    SUBSCRIPTION_DURATION,
                    client.getAccount().getCharacters()
            ));
        }
    }

    private void parseServerSelection(int serverId){
        final GameServer gs = service.getGameServerManager().getSyncServer(serverId);
        if (gs == null){
            client.getSession().write(LoginMessageFormatter.serverSelectionErrorMessage());
        }
        else{
            gs.sendAccountInformations(client.getCipher().getTicket(), client.getAccount()).addListener(new IoFutureListener<WriteFuture>() {
                public void operationComplete(WriteFuture o) {
                    client.getAccount().setConnected(true);
                    client.getSession().write(LoginMessageFormatter.selectedHostInformationMessage(
                            gs.getRemoteAddress(),
                            gs.getRemotePort(),
                            client.getCipher().getTicket(),
                            client.isLocalhost()
                    ));
                    client.getSession().close(false);
                }
            });
        }
    }
}
