package org.d2j.login.service.game;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.enums.WorldStateEnum;
import org.d2j.common.service.D2jNetworkCodecFactory;
import org.d2j.common.service.protocol.Message;
import org.d2j.common.service.protocol.messages.*;
import org.d2j.login.RepositoryManager;
import org.d2j.login.configuration.ILoginConfiguration;
import org.d2j.login.model.LoginAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * User: Blackrush
 * Date: 30/10/11
 * Time: 13:59
 * IDE : IntelliJ IDEA
 */
@Singleton
public class GameServerManager extends Observable implements IoHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameServerManager.class);

    private final IoAcceptor acceptor;
    private final ILoginConfiguration configuration;
    private final RepositoryManager repositoryManager;
    private boolean started;
    private Map<Integer, GameServer> syncServers = new HashMap<>();
    private Map<Integer, GameServer> servers = new HashMap<>();

    @Inject
    public GameServerManager(ILoginConfiguration configuration, RepositoryManager repositoryManager) {
        this.configuration = configuration;
        this.repositoryManager = repositoryManager;

        this.acceptor = new NioSocketAcceptor();
        //this.acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        this.acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new D2jNetworkCodecFactory()));
        this.acceptor.setHandler(this);
        this.acceptor.getSessionConfig().setReadBufferSize(1024);
        this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    public void start() throws IOException {
        if (started) return;

        acceptor.bind(new InetSocketAddress(configuration.getSystemPort()));
        started = true;

        logger.info("listening on {}.", configuration.getSystemPort());
    }

    public  void stop(){
        if (!started) return;

        acceptor.unbind();
        for (IoSession session : acceptor.getManagedSessions().values()){
            if (session.isConnected() || !session.isClosing())
                session.close(false);
        }
        acceptor.dispose();
        started = false;

        logger.info("successfully stoped.");
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }

    public Collection<GameServer> getServers() {
        return servers.values();
    }

    public GameServer getSyncServer(int serverId){
        return syncServers.get(serverId);
    }

    public void sessionCreated(IoSession ioSession) throws Exception {
    }

    public void sessionOpened(IoSession ioSession) throws Exception {
        logger.debug("Session #" + ioSession.getId() + " opened.");

        ioSession.write(new HelloConnectMessage());
    }

    public void sessionClosed(IoSession ioSession) throws Exception {
        GameServer gs = ((GameServer)ioSession.getAttribute("gs"));
        if (gs.isSync()){
            gs.setState(WorldStateEnum.OFFLINE);
            syncServers.remove(gs.getId());
            logger.warn("GameServer #{} closed.", gs.getId());
            notifyObservers();
        }
        else{
            logger.debug("Session #{} closed.", ioSession.getId());
        }
    }

    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        //logger.debug("Session #" + ioSession.getId() + " idle updated : " + idleStatus.toString());
    }

    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.error("Session #" + ioSession.getId() + ". " + throwable.getMessage(), throwable);
        if (ioSession.isConnected()){
            ioSession.close(true);
        }
    }

    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        Message message = (Message)o;
        GameServer gs = (GameServer)ioSession.getAttribute("gs");

        switch (message.getMessageId()){
            case SynchronizationRequestMessage.MESSAGE_ID:
                logger.info("GameServer #{} request a synchronization.", message.getMessageId());
                parseSynchronizationRequestMessage(ioSession, (SynchronizationRequestMessage)message);
                break;

            case ManyCharactersListMessage.MESSAGE_ID:
                parseManyCharactersListMessage(gs, (ManyCharactersListMessage)message);
                break;

            case ServiceInformationsResponseMessage.MESSAGE_ID:
                logger.info("GameServer #{} give its informations.", gs.getId());
                parseServiceInformationsResponseMessage(gs, (ServiceInformationsResponseMessage)message);
                break;

            case ServiceUpdateRequestMessage.MESSAGE_ID:
                logger.info("GameServer #{} refresh its informations.", gs.getId());
                parseServiceUpdateRequestMessage(gs, (ServiceUpdateRequestMessage)message);
                break;

            case CharactersListMessage.MESSAGE_ID:
                parseCharactersListMessage(gs, (CharactersListMessage) message);
                break;

            case ClientDeconnectionMessage.MESSAGE_ID:
                parseClientDeconnectionMessage((ClientDeconnectionMessage)message);
                break;

            case AccountUpdateMessage.MESSAGE_ID:
                parseAccountUpdateMessage((AccountUpdateMessage)message);
                break;

            default:
                logger.warn("Unhandled message #{}.", message.getMessageId());
                break;
        }
    }

    public void messageSent(IoSession ioSession, Object o) throws Exception {
    }

    private void parseSynchronizationRequestMessage(IoSession session, SynchronizationRequestMessage message){
        if (syncServers.containsKey(message.getServerId())){
            session.write(new SynchronizationFailureMessage());
            session.close(false);
        }
        else{
            GameServer gs = servers.get(message.getServerId());
            if (gs == null){
                gs = new GameServer(session);
                gs.setId(message.getServerId());
            }
            else{
                gs.setSession(session);
            }
            session.setAttribute("gs", gs);

            session.write(new ServiceInformationsRequestMessage());
        }
    }

    private void parseManyCharactersListMessage(GameServer gs, ManyCharactersListMessage message) {
        if (syncServers.containsKey(gs.getId())){
            gs.getSession().write(new SynchronizationFailureMessage());
            gs.getSession().close(false);
        }
        else{
            for (Map.Entry<Integer, Integer> entry : message.getCharacters().entrySet()){
                LoginAccount account = repositoryManager.getAccounts().findById(entry.getKey());

                if (account != null){
                    account.getCharacters().remove(gs.getId());
                    account.getCharacters().put(gs.getId(), entry.getValue());
                }
            }

            logger.info("GameServer #{} send accounts' informations.", gs.getId());
        }
    }

    private void parseServiceInformationsResponseMessage(GameServer gs, ServiceInformationsResponseMessage message){
        if (syncServers.containsKey(gs.getId())){
            gs.getSession().write(new SynchronizationFailureMessage());
            gs.getSession().close(false);
        }
        else{
            gs.setRemotePort(message.getRemotePort());
            gs.setRemoteAddress(message.getRemoteAddress());
            gs.setState(message.getState());
            gs.setCompletion(message.getCompletion());

            gs.setSync(true);

            syncServers.put(gs.getId(), gs);
            servers.put(gs.getId(), gs);

            logger.info("GameServer #{} synchronized (session's id = {}).", gs.getId(), gs.getSession().getId());

            gs.getSession().write(new SynchronizationSuccessMessage());

            notifyObservers();
        }
    }

    private void parseServiceUpdateRequestMessage(GameServer gs, ServiceUpdateRequestMessage message){
        if (!syncServers.containsValue(gs)){
            gs.getSession().close(true);
        }
        else{
            gs.setState(message.getState());
            gs.setCompletion(message.getCompletion());

            notifyObservers();
        }
    }

    private void parseCharactersListMessage(GameServer gs, CharactersListMessage message){
        if (!syncServers.containsValue(gs)){
            gs.getSession().close(true);
        }
        else{
            LoginAccount account = repositoryManager.getAccounts().findById(message.getAccountId());
            if (account != null){
                account.getCharacters().remove(gs.getId());
                account.getCharacters().put(gs.getId(), message.getCharactersList());
            }
        }
    }

    private void parseClientDeconnectionMessage(ClientDeconnectionMessage message){
        LoginAccount account = repositoryManager.getAccounts().findById(message.getAccountId());
        if (account != null){
            account.setConnected(false);
        }
    }

    private void parseAccountUpdateMessage(AccountUpdateMessage message) {
        LoginAccount account = repositoryManager.getAccounts().findById(message.getAccountId());
        if (account != null){
            synchronized (account.getLock()){
                account.setRights(message.getPermissions());
            }
        }
    }
}
