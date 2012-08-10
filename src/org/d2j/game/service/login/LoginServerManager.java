package org.d2j.game.service.login;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.d2j.common.service.D2jNetworkCodecFactory;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.messages.*;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.model.GameAccount;
import org.d2j.game.service.IWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;

/**
 * User: Blackrush
 * Date: 31/10/11
 * Time: 16:25
 * IDE : IntelliJ IDEA
 */
@Singleton
public class LoginServerManager implements ILoginServerManager {
    private static final Logger logger = LoggerFactory.getLogger(LoginServerManager.class);

    private final IGameConfiguration configuration;
    private final IWorld world;

    private final IoConnector connector;
    private IoSession session;
    private boolean started;
    private boolean isSynchronized;

    @Inject
    public LoginServerManager(IGameConfiguration configuration, IWorld world) {
        this.configuration = configuration;
        this.world = world;

        this.connector = new NioSocketConnector();
        //this.connector.getFilterChain().addLast("logger", new LoggingFilter());
        this.connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new D2jNetworkCodecFactory()));
        this.connector.setHandler(this);
        this.connector.getSessionConfig().setReadBufferSize(1024);
        this.connector.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    public void start(){
        if (started) return;

        connector.connect(new InetSocketAddress(
                configuration.getSystemAddress(),
                configuration.getSystemPort()
        ));
    }

    public void stop(){
        if (!started) return;

        session.close(false);
        connector.dispose();

        started = false;

        logger.info("successfully closed.");
    }

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public void sessionCreated(IoSession ioSession) throws Exception {

    }

    public void sessionOpened(IoSession ioSession) throws Exception {
        session = ioSession;

        logger.info("successfully connected on {}:{}.",
                configuration.getSystemAddress(),
                configuration.getSystemPort()
        );
    }

    public void sessionClosed(IoSession ioSession) throws Exception {
        if (isSynchronized)
            logger.error("Unsynchronized.");

        isSynchronized = false;
    }

    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        //logger.info("Session idle changed : " + idleStatus.toString());
    }

    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.warn("Unhandled exception. " + throwable.getMessage(), throwable);
    }

    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        Message message = (Message)o;

        logger.debug("Receive " + message.getMessageId() + " from LoginServer");

        switch (message.getMessageId()){
            case HelloConnectMessage.MESSAGE_ID:
                parseHelloConnectMessage();
                break;

            case SynchronizationFailureMessage.MESSAGE_ID:
                logger.error("Synchronization has failed.");
                break;

            case ServiceInformationsRequestMessage.MESSAGE_ID:
                parseServiceInformationsRequestMessage();
                break;

            case SynchronizationSuccessMessage.MESSAGE_ID:
                parseSynchronizationSuccessMessage();
                break;

            case ClientConnectionMessage.MESSAGE_ID:
                parseClientConnectionMessage((ClientConnectionMessage) message);
                break;

            case CharactersListRequestMessage.MESSAGE_ID:
                parseCharactersListRequestMessage((CharactersListRequestMessage)message);
                break;

            default:
                logger.debug("Unhandled message received : " + message.getMessageId());
                break;
        }
    }

    private void parseSynchronizationSuccessMessage() {
        refreshWorld();

        isSynchronized = true;
        logger.info("Synchronization has succeeded.");
    }

    public void messageSent(IoSession ioSession, Object o) throws Exception {
        if (logger.isDebugEnabled()){
            Message message = (Message)o;
            logger.debug("Send " + message.getMessageId());
        }
    }

    private void parseHelloConnectMessage(){
        session.write(new SynchronizationRequestMessage(configuration.getServerId()));
    }

    private void parseServiceInformationsRequestMessage(){
        session.write(new ManyCharactersListMessage(world.getRepositoryManager().getAccounts().map()));

        session.write(new ServiceInformationsResponseMessage(
                configuration.getRemotePort(),
                configuration.getRemoteAddress(),
                world.getState(),
                world.getCompletion()
        ));
    }

    private void parseClientConnectionMessage(ClientConnectionMessage message){
        GameAccount account = world.getRepositoryManager().getAccounts().findById(message.getAccountId());
        if (account == null){
            world.getRepositoryManager().getAccounts().createDefault(
                    message.getAccountId(),
                    message.getNickname(),
                    message.getAnswer(),
                    message.getRights(),
                    message.getCommunity()
            );
        }
        else{
            account.setNickname(message.getNickname());
            account.setAnswer(message.getAnswer());
            account.setRights(message.getRights());
            account.setCommunity(message.getCommunity());
        }

        world.getRepositoryManager().getAccounts().addTicket(message.getTicket(), message.getAccountId());
    }

    private void parseCharactersListRequestMessage(CharactersListRequestMessage message){
        GameAccount account = world.getRepositoryManager().getAccounts().findById(message.getAccountId());
        if (account != null){
            session.write(new CharactersListMessage(
                    account.getId(),
                    account.getCharacters().size()
            ));
        }
    }

    public WriteFuture refreshWorld(){
        return session.write(new ServiceUpdateRequestMessage(
                world.getState(),
                world.getCompletion()
        ));
    }

    public WriteFuture setAccountDeconnected(Integer accountId){
        return session.write(new ClientDeconnectionMessage(accountId));
    }

    public WriteFuture refreshCharactersList(Integer accountId, Integer characters){
        return session.write(new CharactersListMessage(accountId, characters));
    }

    public WriteFuture refreshGameAccount(GameAccount account){
        return session.write(new AccountUpdateMessage(account.getId(), account.getRights()));
    }
}
