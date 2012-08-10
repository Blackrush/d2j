package org.d2j.login.service.login;

import org.d2j.common.StringUtils;
import org.d2j.common.crypto.PasswordCipher;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 21:31
 * IDE : IntelliJ IDEA
 */
public class DofusPasswordCipher extends PasswordCipher {
    @Override
    protected String makeTicket() {
        return StringUtils.random(32);
    }

    @Override
    public String getEncryptedPassword(String clearPassword) {
        StringBuilder encrypted = new StringBuilder();

        for ( int i = 0; i < clearPassword.length(); i++ )
        {
            char ppass = clearPassword.charAt(i);
            char pkey = ticket.charAt(i);

            int apass = ppass / 16;
            int akey = ppass % 16;

            int anb = ( apass + pkey ) % StringUtils.HASH.length();
            int anb2 = ( akey + pkey ) % StringUtils.HASH.length();

            encrypted.append(StringUtils.HASH.charAt(anb));
            encrypted.append(StringUtils.HASH.charAt(anb2));
        }
        return encrypted.toString();
    }
}
