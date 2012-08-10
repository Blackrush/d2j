package org.d2j.common;

import org.apache.mina.core.session.IoSession;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 10:40
 * IDE : IntelliJ IDEA
 */
public class NetworkStringBuffer extends NetworkStringBuilder implements AutoCloseable {
    private final IoSession session;

    public NetworkStringBuffer(IoSession session) {
        this.session = session;
    }

    public void flush(){
        session.write(toString());
    }

    @Override
    public void close() {
        flush();
    }
}
