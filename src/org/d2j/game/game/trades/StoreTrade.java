package org.d2j.game.game.trades;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.GameActionType;
import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.game.items.StoreBag;
import org.d2j.game.model.Character;
import org.d2j.game.model.Item;
import org.d2j.game.model.StoredItem;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 26/02/12
 * Time: 12:03
 * To change this template use File | Settings | File Templates.
 */
public class StoreTrade implements IGameAction, PurchasableTrade, StoreBag.Listener {
    private final GameClient client;
    private final Character seller;

    public StoreTrade(GameClient client, Character seller) {
        this.client = client;
        this.seller = seller;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.STORE;
    }

    @Override
    public void begin() throws GameActionException {
        seller.getStore().addListener(this);

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.STORE, seller.getId()));
            buf.append(TradeGameMessageFormatter.storedItemsListMessage(seller.getStore().toStoreItemType()));
        }
    }

    @Override
    public void end() throws GameActionException {
        seller.getStore().deleteListener(this);
        client.getSession().write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    public void cancel() throws GameActionException {
        end();
    }

    @Override
    public void buy(long itemId, int quantity) throws GameActionException {
        StoredItem sItem = seller.getStore().get(itemId);
        long cost;
        if (sItem == null){
            throw new GameActionException("Unknown item");
        }
        else if (quantity > sItem.getQuantity()){
            throw new GameActionException("Not enough quantity");
        }
        else if ((cost = sItem.getPrice() * quantity) > client.getCharacter().getBag().getKamas()){
            client.getSession().write(InfoGameMessageFormatter.notEnoughKamasMessage());
        }
        else{
            sItem.addQuantity(-quantity);

            boolean add;
            Item item = client.getCharacter().getBag().getSame(sItem.getItem());
            if (item == null){
                item = sItem.getItem().copy();
                item.setOwner(client.getCharacter());
                item.setQuantity(quantity);
                client.getCharacter().getBag().add(item);

                add = true;
            }
            else{
                item.addQuantity(quantity);

                add = false;
            }

            client.getCharacter().getBag().addKamas(-cost);
            seller.getBag().addKamas(cost);

            if (sItem.getQuantity() <= 0){
                seller.getStore().remove(sItem);
            }

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                if (add){
                    buf.append(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
                }
                else{
                    buf.append(ItemGameMessageFormatter.quantityMessage(
                            item.getId(),
                            item.getQuantity()
                    ));
                }

                buf.append(TradeGameMessageFormatter.buySuccessMessage());
                buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
                buf.append(TradeGameMessageFormatter.storedItemsListMessage(seller.getStore().toStoreItemType()));
            }

            if (seller.getStore().empty()){
                end();
                seller.getStore().setActive(false);

                client.getActions().pop();
            }
        }
    }

    @Override
    public void listen(Character seller) {
        try {
            end();
            client.getActions().pop();
        } catch (GameActionException e) {
            e.printStackTrace();
        }
    }
}
