package org.d2j.game.game.trades;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.GameActionType;
import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.model.Item;
import org.d2j.game.model.StoredItem;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 24/02/12
 * Time: 07:31
 * To change this template use File | Settings | File Templates.
 */
public class ManageStoreTrade implements IGameAction {
    private final GameClient client;

    public ManageStoreTrade(GameClient client) {
        this.client = client;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.STORE_MANAGEMENT;
    }

    public void add(long itemId, int quantity, long price) throws GameActionException {
        Item item = client.getCharacter().getBag().get(itemId);

        if (item == null){
            throw new GameActionException("Unknown item");
        }
        else if (quantity > item.getQuantity()){
            throw new GameActionException("You have not got enough quantity");
        }

        StoredItem sItem = client.getCharacter().getStore().get(item.getId());
        if (sItem != null){
            sItem.addQuantity(quantity);
            item.addQuantity(-quantity);
            sItem.setPrice(price);
        }
        else{
            client.getCharacter().getStore().add(item, quantity, price);
            item.addQuantity(-quantity);
        }

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            if (item.getQuantity() > 0){
                buf.append(ItemGameMessageFormatter.quantityMessage(
                        item.getId(),
                        item.getQuantity()
                ));
            }
            else{
                buf.append(ItemGameMessageFormatter.deleteMessage(item.getId()));
            }

            buf.append(TradeGameMessageFormatter.
                    storedItemsListMessage(client.getCharacter().getStore().toStoreItemType()));
            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                    client.getCharacter().getStatistics().getUsedPods(),
                    client.getCharacter().getStatistics().getMaxPods()
            ));
        }
    }

    public void update(long itemId, long price) throws GameActionException {
        StoredItem sItem = client.getCharacter().getStore().get(itemId);

        if (sItem == null){
            throw new GameActionException("Unknown item");
        }

        sItem.setPrice(price);

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(TradeGameMessageFormatter.
                    storedItemsListMessage(client.getCharacter().getStore().toStoreItemType()));
            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                    client.getCharacter().getStatistics().getUsedPods(),
                    client.getCharacter().getStatistics().getMaxPods()
            ));
        }
    }

    public void remove(long itemId) throws GameActionException {
        StoredItem sItem = client.getCharacter().getStore().remove(itemId);
        if (sItem == null) {
            throw new GameActionException("Unknown item");
        }
        else{
            boolean add = sItem.getItem().getQuantity() <= 0;
            sItem.getItem().addQuantity(sItem.getQuantity());

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                if (add){
                    buf.append(ItemGameMessageFormatter.addItemMessage(sItem.getItem().toBaseItemType()));
                }
                else{
                    buf.append(ItemGameMessageFormatter.quantityMessage(
                            sItem.getItem().getId(),
                            sItem.getItem().getQuantity()
                    ));
                }

                buf.append(TradeGameMessageFormatter.
                        storedItemsListMessage(client.getCharacter().getStore().toStoreItemType()));
                buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                        client.getCharacter().getStatistics().getUsedPods(),
                        client.getCharacter().getStatistics().getMaxPods()
                ));
            }
        }
    }

    @Override
    public void begin() throws GameActionException {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())) {
            buf.append(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.STORE_MANAGEMENT));
            buf.append(TradeGameMessageFormatter.
                    storedItemsListMessage(client.getCharacter().getStore().toStoreItemType()));
        }
    }

    @Override
    public void end() throws GameActionException {
        client.getSession().write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    public void cancel() throws GameActionException {
        client.getSession().write(TradeGameMessageFormatter.tradeQuitMessage());
    }
}
