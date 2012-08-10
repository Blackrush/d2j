package org.d2j.common.service;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.d2j.common.service.D2jNetworkDecoder;
import org.d2j.common.service.D2jNetworkEncoder;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 10:24
 * IDE : IntelliJ IDEA
 */
public class D2jNetworkCodecFactory implements ProtocolCodecFactory {
    private D2jNetworkDecoder decoder;
    private D2jNetworkEncoder encoder;

    public D2jNetworkCodecFactory() {
        decoder = new D2jNetworkDecoder();
        encoder = new D2jNetworkEncoder();
    }

    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return decoder;
    }
}
