package org.d2j.login.service.login.handler;

import org.d2j.common.client.protocol.LoginMessageFormatter;
import org.d2j.login.service.login.LoginClient;
import org.d2j.login.service.login.LoginClientHandler;
import org.d2j.login.service.login.LoginService;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:37
 * IDE : IntelliJ IDEA
 */
public class VersionHandler extends LoginClientHandler {
    public VersionHandler(LoginService service, LoginClient client) {
        super(service, client);
    }

    @Override
    public void parse(String packet) throws Exception {
        if (!service.getConfiguration().getClientVersion().equalsIgnoreCase(packet)){
            client.getSession().write(LoginMessageFormatter.badClientVersion(service.getConfiguration().getClientVersion()));
            client.getSession().close(false);
        }
        else{
            client.setHandler(new AuthenticationHandler(service, client));
        }
    }

    @Override
    public void onClosed() {

    }
}
