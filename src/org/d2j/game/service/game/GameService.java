package org.d2j.game.service.game;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.d2j.common.client.protocol.BasicGameMessageFormatter;
import org.d2j.game.configuration.IGameConfiguration;
import org.d2j.game.game.PubSystem;
import org.d2j.game.game.commands.CommandFactory;
import org.d2j.game.game.commands.ICommandFactory;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.handler.ServerScreenHandler;
import org.d2j.utils.Action0;
import org.d2j.utils.Future0;
import org.d2j.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 09:42
 * IDE : IntelliJ IDEA
 */
@Singleton
public class GameService implements IoHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final IGameConfiguration configuration;
    private final IWorld world;
    private final PubSystem pubSystem;
    private final ICommandFactory commandFactory;

    private final IoAcceptor acceptor;
    private boolean started;

    @Inject
    public GameService(IGameConfiguration configuration, IWorld world) {
        this.configuration = configuration;
        this.world = world;
        this.pubSystem = new PubSystem(configuration, world);
        this.commandFactory = new CommandFactory(world);

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

    public void start() {
        if (started) return;

        ThreadUtils.whileFailure(new Action0() {
            @Override
            public void action() throws Exception {
                acceptor.bind(new InetSocketAddress(configuration.getRemotePort()));
            }
        }, 5000, logger).await();

        pubSystem.start();
        logger.info("{} commands loaded.", commandFactory.init());

        started = true;

        logger.info("listening on {}:{}.",
                configuration.getRemoteAddress(),
                configuration.getRemotePort()
        );
    }

    public void stop(){
        if (!started) return;

        pubSystem.stop();

        acceptor.unbind();
        for (IoSession session : acceptor.getManagedSessions().values()){
            if (session.isConnected() || !session.isClosing())
                session.close(false);
        }
        acceptor.dispose();

        started = false;

        logger.info("successfully stoped.");
    }

    public IGameConfiguration getConfiguration() {
        return configuration;
    }

    public IWorld getWorld() {
        return world;
    }

    public boolean isStarted() {
        return started;
    }

    public ICommandFactory getCommandFactory() {
        return commandFactory;
    }

    public PubSystem getPubSystem() {
        return pubSystem;
    }

    public int getRecordConnected(){
        return acceptor.getStatistics().getLargestManagedSessionCount();
    }

    public List<GameClient> getOnlinePlayers(){
        List<GameClient> result = new ArrayList<>(acceptor.getManagedSessionCount());
        for (IoSession session : acceptor.getManagedSessions().values()){
            result.add((GameClient) session.getAttribute("client"));
        }
        return result;
    }

    public void sessionCreated(IoSession ioSession) throws Exception {
        GameClient client = new GameClient(this, ioSession);
        client.setHandler(new ServerScreenHandler(this, client));

        ioSession.setAttribute("client", client);
    }

    public void sessionOpened(IoSession ioSession) throws Exception {
        logger.debug("Session #" + ioSession.getId() + " opened.");
    }

    public void sessionClosed(IoSession ioSession) throws Exception {
        logger.debug("Session #" + ioSession.getId() + " closed.");
        ((GameClient)ioSession.getAttribute("client")).getHandler().onClosed();
    }

    public void sessionIdle(IoSession ioSession, IdleStatus idleStatus) throws Exception {
        logger.debug("Session #" + ioSession.getId() + " idle changed : " + idleStatus.toString());
    }

    public void exceptionCaught(IoSession ioSession, Throwable throwable) throws Exception {
        logger.error("Session #" + ioSession.getId() + ": " + throwable.getMessage(), throwable);

        if (configuration.getDebugMode()){
            ioSession.close(true);
        }
        else{
            ioSession.write(BasicGameMessageFormatter.noOperationMessage());
        }
    }

    public void messageReceived(IoSession ioSession, Object o) throws Exception {
        String packet = (String)o;

        logger.info("Receive " + packet.length() + " bytes from Session #" + ioSession.getId() + " : " + packet);

        if (packet.equals("ping")){
            ioSession.write("pong");
        }
        else if (packet.equals("qping")){
            ioSession.write("qpong");
        }
        else{
            ((GameClient)ioSession.getAttribute("client")).getHandler().parse(packet);
        }
    }

    public void messageSent(IoSession ioSession, Object o) throws Exception {
        //if (logger.isDebugEnabled()){
            String packet = (String)o;
            logger.info("Send " + packet.length() + " bytes to Session #" + ioSession.getId() + " : " + packet);
        //}
    }
}
