package org.d2j.game.game.trades;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.GameActionType;
import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.game.items.Bag;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.service.game.GameClient;
import org.d2j.utils.database.repository.IEntityRepository;

import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 14/01/12
 * Time: 09:40
 * To change this template use File | Settings | File Templates.
 */
public class PlayerTrade implements IGameAction {
    private final IEntityRepository<Item, Long> items;
    private final Trader source, target;

    public PlayerTrade(IEntityRepository<Item, Long> items, GameClient source, GameClient target) {
        this.items = items;

        this.source = new Trader(this, source, target);
        this.source.getClient().setTrader(this.source);

        this.target = new Trader(this, target, source);
        this.target.getClient().setTrader(this.target);
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.PLAYER_TRADE;
    }

    @Override
    public void begin() throws GameActionException {
        String packet = TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.PLAYER);

        source.getClient().getSession().write(packet);
        target.getClient().getSession().write(packet);
    }

    @Override
    public void end() throws GameActionException {
        for (Trader trader : Arrays.asList(source, target)){
            Character opposite = trader.getOpposite().getCharacter();

            try (NetworkStringBuffer buf = new NetworkStringBuffer(trader.getOpposite().getSession())){
                for (Item item : trader.getBag().values()){
                    Item sameItem;
                    if ((sameItem = opposite.getBag().getSame(item)) != null){
                        sameItem.addQuantity(item.getQuantity());

                        buf.append(ItemGameMessageFormatter.
                                quantityMessage(sameItem.getId(), sameItem.getQuantity()));

                        items.delete(item);
                    }
                    else{
                        opposite.getBag().add(item);

                        buf.append(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
                    }
                }

                opposite.getBag().addKamas(trader.getBag().getKamas());
                trader.getClient().getCharacter().getBag().addKamas(-trader.getBag().getKamas());

                buf.append(opposite.getStatistics().getStatisticsMessage());
                buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                        trader.getClient().getCharacter().getStatistics().getUsedPods(),
                        trader.getClient().getCharacter().getStatistics().getMaxPods()
                ));
                buf.append(TradeGameMessageFormatter.tradeSuccessMessage());
            }
        }

        source.getClient().setTrader(null);
        source.getClient().getActions().pop();
        target.getClient().setTrader(null);
        target.getClient().getActions().pop();
    }

    @Override
    public void cancel() throws GameActionException {
        String packet = TradeGameMessageFormatter.tradeQuitMessage();

        source.getClient().getSession().write(packet);
        target.getClient().getSession().write(packet);
    }

    public void add(Trader trader, Item item, int quantity) throws GameActionException {
        if (item == null){
            throw new GameActionException("Unknown item.");
        }
        else if (quantity > item.getQuantity()){
            throw new GameActionException("You have not got enough quantity.");
        }

        Item clone = trader.getBag().get(item.getId());

        if (quantity == item.getQuantity()){
            if (clone == null){
                trader.getBag().add(item);
                trader.getClient().getCharacter().getBag().removeEntity(item.getId());

                putItem(trader, item);
            }
            else{
                clone.addQuantity(quantity);
                item.setQuantity(0);

                putItem(trader, clone);
            }

            trader.getClient().getSession().write(ItemGameMessageFormatter.deleteMessage(item.getId()));
        }
        else{
            if (clone == null){
                clone = item.copy();
                clone.setOwner(trader.getClient().getCharacter());
                clone.setQuantity(quantity);
                item.addQuantity(-quantity);
                trader.getBag().add(clone);

                putItem(trader, clone);
            }
            else{
                clone.addQuantity(quantity);
                item.addQuantity(-quantity);

                putItem(trader, clone);
            }

            trader.getClient().getSession().write(ItemGameMessageFormatter.
                    quantityMessage(item.getId(), item.getQuantity()));
        }
    }

    public void remove(Trader trader, Item item, int quantity) throws GameActionException {
        if (item == null){
            throw new GameActionException("Unknown item.");
        }
        else if (quantity > item.getQuantity()){
            throw new GameActionException("You have not enough quantity.");
        }

        Item clone = trader.getClient().getCharacter().getBag().get(item.getId());

        if (quantity == item.getQuantity()){
            if (clone == null){
                trader.getClient().getCharacter().getBag().put(item.getId(), item);
                trader.getBag().remove(item.getId());

                trader.getClient().getSession().write(ItemGameMessageFormatter.
                        addItemMessage(item.toBaseItemType()));
            }
            else{
                clone.addQuantity(quantity);
                item.setQuantity(0);

                trader.getClient().getSession().write(ItemGameMessageFormatter.
                        quantityMessage(clone.getId(), clone.getQuantity()));
            }

            removeItem(trader, item);
        }
        else{
            if (clone == null){
                clone = item.copy();
                clone.setOwner(trader.getClient().getCharacter());
                clone.setQuantity(quantity);
                item.addQuantity(-quantity);

                trader.getClient().getCharacter().getBag().add(clone);

                trader.getClient().getSession().write(ItemGameMessageFormatter.
                        addItemMessage(clone.toBaseItemType()));
            }
            else{
                clone.addQuantity(quantity);
                item.addQuantity(-quantity);

                trader.getClient().getSession().write(ItemGameMessageFormatter.
                        quantityMessage(clone.getId(), clone.getQuantity()));
            }

            putItem(trader, item);
        }
    }

    public void putKamas(Trader trader, long kamas) throws GameActionException {
        if (kamas == 0){
            throw new GameActionException("You can not put null number of kamas.");
        }

        Bag bag1 = kamas > 0 ? trader.getClient().getCharacter().getBag() : trader.getBag();
        Bag bag2 = kamas > 0 ? trader.getBag() : trader.getClient().getCharacter().getBag();

        if (bag1.getKamas() < kamas){
            throw new GameActionException("You have not enough kamas.");
        }
        else{
            bag1.addKamas(-kamas);
            bag2.addKamas(kamas);

            trader.getClient().getSession().write(TradeGameMessageFormatter.
                    tradeLocalSetKamasMessage(trader.getBag().getKamas()));

            trader.getOpposite().getSession().write(TradeGameMessageFormatter.
                    tradeRemoteSetKamasMessage(trader.getBag().getKamas()));
        }
    }

    public void setReady(Trader trader, boolean ready) throws GameActionException {
        if (trader.getTrade() != this){
            throw new GameActionException("It is not your trade.");
        }

        String packet = TradeGameMessageFormatter.
                tradeReadyMessage(ready, trader.getClient().getCharacter().getId());

        trader.getClient().getSession().write(packet);
        trader.getOpposite().getSession().write(packet);

        if (trader.isReady() && trader.getOpposite().getTrader().isReady()){
            end();
        }
    }

    private void putItem(Trader trader, Item item) throws GameActionException {
        trader.getClient().getSession().write(TradeGameMessageFormatter.
                tradeLocalPutItemMessage(item.getId(), item.getQuantity()));

        trader.getOpposite().getSession().write(TradeGameMessageFormatter.
                tradeRemotePutItemMessage(item.toBaseItemType()));
    }

    private void removeItem(Trader trader, Item item) throws GameActionException {
        trader.getClient().getSession().write(TradeGameMessageFormatter.
                tradeLocalRemoveItemMessage(item.getId()));

        trader.getOpposite().getSession().write(TradeGameMessageFormatter.
                tradeRemoteRemoveItemMessage(item.getId()));
    }
}
