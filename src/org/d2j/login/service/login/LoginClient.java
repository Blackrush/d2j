package org.d2j.login.service.login;

import org.apache.mina.core.session.IoSession;
import org.d2j.common.crypto.PasswordCipher;
import org.d2j.common.client.protocol.LoginMessageFormatter;
import org.d2j.login.model.LoginAccount;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:23
 * IDE : IntelliJ IDEA
 */
public class LoginClient {
    private final IoSession session;
    private LoginClientHandler handler;
    private final PasswordCipher cipher;
    private LoginAccount account;

    public LoginClient(IoSession session, PasswordCipher cipher) {
        this.session = session;
        this.cipher = cipher;

        this.session.write(LoginMessageFormatter.helloWorld(cipher.getTicket()));
    }

    public IoSession getSession() {
        return session;
    }

    public LoginClientHandler getHandler() {
        return handler;
    }

    public void setHandler(LoginClientHandler handler) {
        this.handler = handler;
    }

    public PasswordCipher getCipher(){
        return cipher;
    }

    public LoginAccount getAccount() {
        return account;
    }

    public void setAccount(LoginAccount account) {
        this.account = account;
    }

    public boolean isLocalhost(){
        return session.getRemoteAddress().toString().contains("127.0.0.1");
    }
}
