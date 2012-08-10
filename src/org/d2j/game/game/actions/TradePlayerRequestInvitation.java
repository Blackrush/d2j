package org.d2j.game.game.actions;

import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.game.game.trades.PlayerTrade;
import org.d2j.game.service.IWorld;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 14/01/12
 * Time: 09:20
 * To change this template use File | Settings | File Templates.
 */
public class TradePlayerRequestInvitation implements IInvitation {
    private final IWorld world;
    private final GameClient source, target;

    public TradePlayerRequestInvitation(IWorld world, GameClient source, GameClient target) {
        this.world = world;
        this.source = source;
        this.target = target;
    }

    @Override
    public void accept() throws GameActionException {
        source.getActions().pop();
        target.getActions().pop();

        PlayerTrade trade = new PlayerTrade(
                world.getRepositoryManager().getItems(),
                source, target
        );
        source.getActions().push(trade);
        target.getActions().push(trade);

        trade.begin();
    }

    @Override
    public void decline() throws GameActionException {
        String packet = TradeGameMessageFormatter.tradeQuitMessage();

        source.getSession().write(packet);
        target.getSession().write(packet);
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.TRADE_PLAYER_REQUEST_INVITATION;
    }

    @Override
    public void begin() throws GameActionException {
        String packet = TradeGameMessageFormatter.tradeRequestMessage(
                source.getCharacter().getId(),
                target.getCharacter().getId(),
                TradeTypeEnum.PLAYER
        );

        source.getSession().write(packet);
        target.getSession().write(packet);
    }

    @Override
    public void end() throws GameActionException {
        decline();
    }

    @Override
    public void cancel() throws GameActionException {
        decline();
    }
}
