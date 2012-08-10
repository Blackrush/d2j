package org.d2j.login.service.login.handler;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.LoginMessageFormatter;
import org.d2j.login.model.LoginAccount;
import org.d2j.login.service.game.GameServer;
import org.d2j.login.service.login.LoginClient;
import org.d2j.login.service.login.LoginClientHandler;
import org.d2j.login.service.login.LoginService;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:40
 * IDE : IntelliJ IDEA
 */
public class AuthenticationHandler extends LoginClientHandler {
    public AuthenticationHandler(LoginService service, LoginClient client) {
        super(service, client);
    }

    @Override
    public void parse(String packet) throws Exception {
        if (packet.startsWith("Af"))
            return;

        String[] args = packet.split("\n");
        if (args.length != 2)
            throw new Exception("Bad data received : args must have a length of 2.");

        String username = args[0],
               password = args[1].substring(2);

        LoginAccount account = service.getRepositoryManager().getAccounts().findByName(username);

        if (account == null){
            client.getSession().write(LoginMessageFormatter.accessDenied());
            client.getSession().close(false);
        }
        else if (account.getRights() == Permissions.BANNED){
            client.getSession().write(LoginMessageFormatter.banned());
            client.getSession().close(false);
        }
        else if (account.isConnected()){
            client.getSession().write(LoginMessageFormatter.alreadyConnected());
            client.getSession().close(false);
        }
        else if (!client.getCipher().getEncryptedPassword(account.getPassword()).equalsIgnoreCase(password)){
            client.getSession().write(LoginMessageFormatter.accessDenied());
            client.getSession().close(false);
        }
        else{
            account.setConnected(true);
            client.setAccount(account);
            client.setHandler(new ServerScreenHandler(service, client));

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                buf.append(LoginMessageFormatter.nicknameInformationMessage(account.getNickname()));
                buf.append(LoginMessageFormatter.communityInformationMessage(service.getConfiguration().getCommunity()));
                buf.append(LoginMessageFormatter.serversInformationsMessage(
                        GameServer.toGameServerType(service.getGameServerManager().getServers()),
                        true
                ));
                buf.append(LoginMessageFormatter.identificationSuccessMessage(account.getRights().ordinal() > Permissions.MEMBER.ordinal()));
                buf.append(LoginMessageFormatter.accountQuestionInformationMessage(account.getQuestion()));
            }
        }
    }

    @Override
    public void onClosed() {

    }
}
