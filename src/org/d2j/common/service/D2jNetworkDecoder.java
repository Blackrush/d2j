package org.d2j.common.service;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.MessageFactory;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 14:19
 * IDE : IntelliJ IDEA
 */
public class D2jNetworkDecoder extends CumulativeProtocolDecoder {
    @Override
    protected boolean doDecode(IoSession ioSession, IoBuffer buf, ProtocolDecoderOutput out) throws Exception {
        if (buf.remaining() < 8) return false;

        int messageid = buf.getInt(),
            length    = buf.getInt();

        if (buf.remaining() >= length){
            Message message = MessageFactory.getInstance().getMessage(messageid);
            if (message != null){
                message.deserialize(buf);
                out.write(message);
            }

            if (buf.remaining() > 0)
                return false;

            return true;
        }
        else{
            return false;
        }
    }
}
