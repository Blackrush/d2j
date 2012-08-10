package org.d2j.common.crypto;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 19:38
 * IDE : IntelliJ IDEA
 */
public abstract class PasswordCipher {

    protected String ticket;

    protected PasswordCipher(){
        ticket = makeTicket();
    }

    protected abstract String makeTicket();

    public String getTicket(){
        return ticket;
    }

    public abstract String getEncryptedPassword(String clearPassword);
}
