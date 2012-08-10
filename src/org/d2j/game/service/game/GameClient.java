package org.d2j.game.service.game;

import org.apache.mina.core.future.IoFutureListener;
import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;
import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.ApproachGameMessageFormatter;
import org.d2j.common.client.protocol.ChannelGameMessageFormatter;
import org.d2j.common.client.protocol.GameMessageFormatter;
import org.d2j.game.game.Party;
import org.d2j.game.game.actions.ActionList;
import org.d2j.game.game.actions.GameActionType;
import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.game.logging.ConsoleLogger;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.game.logging.TchatLogger;
import org.d2j.game.game.trades.Trader;
import org.d2j.game.model.Character;
import org.d2j.game.model.GameAccount;
import org.d2j.game.service.game.handler.FightHandler;

import java.util.Stack;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 09:48
 * IDE : IntelliJ IDEA
 */
public class GameClient {
    private final IoSession session;
    private final GameService service;
    private GameClientHandler handler;
    private ActionList actions = new ActionList(this);

    private GameAccount account;
    private Character character;
    private Party party;
    private Trader trader;

    private TchatLogger tchatOut;
    private ConsoleLogger consoleOut;

    public GameClient(GameService service, IoSession s) {
        this.service = service;
        this.session = s;
        this.tchatOut = new TchatLogger(s);
        this.consoleOut = new ConsoleLogger(s);

        this.session.write(ApproachGameMessageFormatter.helloGameMessage());
    }

    public IoSession getSession() {
        return session;
    }

    public GameService getService() {
        return service;
    }

    public String getRemoteAddress(){
        String address = session.getRemoteAddress().toString();
        return address.substring(1, address.indexOf(':'));
    }

    public boolean isLocalhost(){
        String address = getRemoteAddress();
        return address.equals("127.0.0.1") || address.equals("localhost");
    }

    public void kick(){
        session.close(true);
    }

    public void kick(Character kicker, String reason){
        session.write(GameMessageFormatter.kickMessage(kicker.getName(), reason));
        session.close(false);
    }

    public void kick(String reason){
        session.write(GameMessageFormatter.kickMessage("System", reason));
        session.close(false);
    }

    public GameClientHandler getHandler() {
        return handler;
    }

    public void setHandler(GameClientHandler handler) {
        this.handler = handler;
    }

    public GameAccount getAccount() {
        return account;
    }

    public void setAccount(GameAccount account) {
        this.account = account;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public ActionList getActions() {
        return actions;
    }

    public boolean isBusy(){
        return actions.size() > 0 && !(handler instanceof FightHandler);
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public Trader getTrader() {
        return trader;
    }

    public void setTrader(Trader trader) {
        this.trader = trader;
    }

    public DofusLogger getTchatOut() {
        return tchatOut;
    }

    public DofusLogger getConsoleOut() {
        return consoleOut;
    }
}
