package org.d2j.common.service.protocol;

import org.d2j.common.service.protocol.messages.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 14:57
 * IDE : IntelliJ IDEA
 */
public final class MessageFactory {
    private static final Logger logger = LoggerFactory.getLogger(MessageFactory.class);
    private static MessageFactory self = new MessageFactory();
    public static MessageFactory getInstance() {
        return self;
    }

    private Map<Integer, MessageMaker> messages = new HashMap<>();
    private boolean inited;

    private MessageFactory() {
    }

    public Message getMessage(Integer messageId) {
        MessageMaker maker = messages.get(messageId);
        return maker != null ? maker.make() : null;
    }

    public void init(){
        if (inited) return;

        _init();
        logger.info("{} messages loaded.", messages.size());

        inited = true;
    }

    private void _init(){
        messages.put(CharactersListRequestMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new CharactersListRequestMessage();
            }
        });

        messages.put(CharactersListMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new CharactersListMessage();
            }
        });

        messages.put(ClientConnectionMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ClientConnectionMessage();
            }
        });

        messages.put(ClientDeconnectionMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ClientDeconnectionMessage();
            }
        });

        messages.put(HelloConnectMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new HelloConnectMessage();
            }
        });

        messages.put(ServiceInformationsRequestMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ServiceInformationsRequestMessage();
            }
        });

        messages.put(ServiceInformationsResponseMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ServiceInformationsResponseMessage();
            }
        });

        messages.put(ServiceUpdateRequestMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ServiceUpdateRequestMessage();
            }
        });

        messages.put(SynchronizationFailureMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new SynchronizationFailureMessage();
            }
        });

        messages.put(SynchronizationRequestMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new SynchronizationRequestMessage();
            }
        });

        messages.put(SynchronizationSuccessMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new SynchronizationSuccessMessage();
            }
        });

        messages.put(ManyCharactersListMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new ManyCharactersListMessage();
            }
        });

        messages.put(AccountUpdateMessage.MESSAGE_ID, new MessageMaker() {
            @Override
            public Message make() {
                return new AccountUpdateMessage();
            }
        });
    }
}
