package org.d2j.login.service.login;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.d2j.login.RepositoryManager;
import org.d2j.login.configuration.ILoginConfiguration;
import org.d2j.login.service.game.GameServerManager;
import org.d2j.login.service.login.handler.VersionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * User: Blackrush
 * Date: 29/10/11
 * Time: 17:05
 * IDE : IntelliJ IDEA
 */
@Singleton
public class LoginService implements IoHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private final IoAcceptor acceptor;
    private final ILoginConfiguration configuration;
    private boolean started;
    private final RepositoryManager repositoryManager;
    private final GameServerManager gameServerManager;

    @Inject
    public LoginService(ILoginConfiguration configuration, RepositoryManager repositoryManager, GameServerManager gameServerManager) {
        this.configuration = configuration;
        this.repositoryManager = repositoryManager;
        this.gameServerManager = gameServerManager;

        this.acceptor = new NioSocketAcceptor();
        //this.acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        this.acceptor.getFilterChain().addLast("codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(
                        Charset.forName("UTF8"),
                        LineDelimiter.NUL,
                        new LineDelimiter("\n\0")
                )));
        this.acceptor.setHandler(this);
        this.acceptor.getSessionConfig().setReadBufferSize(1024);
        this.acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    }

    public void start() throws IOException {
        if (started) return;

        acceptor.bind(new InetSocketAddress(configuration.getRemotePort()));
        started = true;

        logger.info("listening on {}.", configuration.getRemotePort());
    }

    public void stop() {
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

    public ILoginConfiguration getConfiguration() {
        return configuration;
    }

    public RepositoryManager getRepositoryManager() {
        return repositoryManager;
    }

    public GameServerManager getGameServerManager() {
        return gameServerManager;
    }

    public void sessionCreated(IoSession ioSession) throws Exception {
        LoginClient client = new LoginClient(ioSession, new DofusPasswordCipher());
        client.setHandler(new VersionHandler(this, client));
        ioSession.setAttribute("client", client);
    }

    public void sessionOpened(IoSession ioSession) throws Exception {
        logger.debug(ioSession.getId() + " connected.");
    }

    public void sessionClosed(IoSession ioSession) throws Exception {
        ((LoginClient)ioSession.getAttribute("client")).getHandler().onClosed();

        logger.debug(ioSession.getId() + " disconnected.");
    }

    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        logger.debug(ioSession.getId() + " idle status : " + idleStatus.toString());
    }

    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.error(ioSession.getId() + " has thrown a exception.", throwable);
    }

    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        String packet = (String)o;
        logger.info(ioSession.getId() + " receive " + packet.length() + " bytes : " + packet);

        ((LoginClient)ioSession.getAttribute("client")).getHandler().parse(packet);
    }

    public void messageSent(IoSession ioSession, Object o) throws Exception {
        String packet = (String)o;
        logger.info(ioSession.getId() + " send " + packet.length() + " bytes : " + packet);
    }
}
