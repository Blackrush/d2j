package org.d2j.login.service.login;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:34
 * IDE : IntelliJ IDEA
 */
public abstract class LoginClientHandler {
    protected LoginService service;
    protected LoginClient client;

    protected LoginClientHandler(LoginService service, LoginClient client) {
        this.service = service;
        this.client = client;
    }

    public abstract void parse(String packet) throws Exception;
    public abstract void onClosed();
}
