package org.d2j.game.game.trades;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.common.client.protocol.TradeGameMessageFormatter;
import org.d2j.common.client.protocol.enums.TradeTypeEnum;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.GameActionType;
import org.d2j.game.game.actions.IGameAction;
import org.d2j.game.model.Item;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.model.NpcTemplate;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 01/02/12
 * Time: 18:23
 * To change this template use File | Settings | File Templates.
 */
public class NpcTrade implements IGameAction, PurchasableTrade {
    private final GameClient client;
    private final NpcTemplate npc;

    public NpcTrade(GameClient client, NpcTemplate npc) {
        this.client = client;
        this.npc = npc;
    }

    @Override
    public GameActionType getActionType() {
        return GameActionType.NPC_TRADE;
    }

    @Override
    public void begin() throws GameActionException {
        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            buf.append(TradeGameMessageFormatter.startTradeMessage(TradeTypeEnum.NPC));
            buf.append(npc.getSells().getCache().get());
        }
    }

    @Override
    public void end() throws GameActionException {
        client.getSession().write(TradeGameMessageFormatter.tradeQuitMessage());
    }

    @Override
    public void cancel() throws GameActionException {
        end();
    }

    @Override
    public void buy(long itemId, int quantity) throws GameActionException {
        ItemTemplate tpl = npc.getSells().get((int)itemId);
        if (tpl == null){
            throw new GameActionException("Unknown item #" + itemId);
        }
        else if (client.getCharacter().getBag().isFull()){
            client.getSession().write(TradeGameMessageFormatter.buyErrorMessage());
        }
        else if (tpl.getPrice() * quantity > client.getCharacter().getBag().getKamas()){
            client.getSession().write(TradeGameMessageFormatter.buyErrorMessage());
        }
        else{
            long cost = tpl.getPrice() * quantity;
            client.getCharacter().getBag().addKamas(-cost);

            Item item = tpl.generate(), same;
            item.setOwner(client.getCharacter());
            item.setQuantity(quantity);

            try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
                if ((same = client.getCharacter().getBag().getSame(item)) != null){
                    same.addQuantity(quantity);

                    buf.append(ItemGameMessageFormatter.
                            quantityMessage(same.getId(), same.getQuantity()));
                }
                else{
                    client.getCharacter().getBag().add(item);

                    buf.append(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
                }

                buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
                buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                        client.getCharacter().getStatistics().getUsedPods(),
                        client.getCharacter().getStatistics().getMaxPods()
                ));
                buf.append(TradeGameMessageFormatter.buySuccessMessage());
            }
        }
    }

    public void sell(Item item, int quantity) throws GameActionException {
        if (quantity > item.getQuantity()) {
            quantity = item.getQuantity();
        }

        long kamas = item.getTemplate().getPrice() * quantity / 10;

        client.getCharacter().getBag().addKamas(kamas);
        item.addQuantity(-quantity);

        if (item.getQuantity() <= 0){
            client.getCharacter().getBag().remove(item.getId());
        }

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            if (item.getQuantity() <= 0){
                buf.append(ItemGameMessageFormatter.deleteMessage(item.getId()));
            }
            else{
                buf.append(ItemGameMessageFormatter.quantityMessage(item.getId(), item.getQuantity()));
            }

            buf.append(client.getCharacter().getStatistics().getStatisticsMessage());
            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                    client.getCharacter().getStatistics().getUsedPods(),
                    client.getCharacter().getStatistics().getMaxPods()
            ));
            buf.append(TradeGameMessageFormatter.sellSuccessMessage());
        }
    }
}
