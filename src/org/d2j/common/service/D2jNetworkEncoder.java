package org.d2j.common.service;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.d2j.common.service.protocol.Message;

import java.nio.ByteBuffer;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 14:18
 * IDE : IntelliJ IDEA
 */
public class D2jNetworkEncoder extends ProtocolEncoderAdapter {
    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput out) throws Exception {
        if (o instanceof IoBuffer){
            out.write(o);
            return;
        }

        if (!(o instanceof Message))
            throw new Exception("o must be a Message");

        Message message = (Message)o;

        IoBuffer buf = IoBuffer.allocate(1024);
        buf.setAutoExpand(true);

        buf.position(8);
        message.serialize(buf);
        int length = buf.position() - 8;

        buf.putInt(0, message.getMessageId());
        buf.putInt(4, length);

        buf.flip();

        out.write(buf);
    }
}
