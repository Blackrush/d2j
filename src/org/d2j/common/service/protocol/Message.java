package org.d2j.common.service.protocol;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 14:21
 * IDE : IntelliJ IDEA
 */
public abstract class Message {
    public abstract int getMessageId();

    public abstract void serialize(IoBuffer buffer);
    public abstract void deserialize(IoBuffer buffer);
}
