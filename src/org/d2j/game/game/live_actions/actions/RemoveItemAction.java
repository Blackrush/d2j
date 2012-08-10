package org.d2j.game.game.live_actions.actions;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.game.game.actions.GameActionException;
import org.d2j.game.game.actions.NpcDialogAction;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.model.Item;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 03/02/12
 * Time: 23:06
 * To change this template use File | Settings | File Templates.
 */
public class RemoveItemAction implements ILiveAction {
    private final ItemTemplate template;
    private final int quantity;

    public RemoveItemAction(ItemTemplate template, int quantity) {
        this.template = template;
        this.quantity = quantity;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.REMOVE_ITEM;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        Item item = client.getCharacter().getBag().getByTemplate(template);

        if (item == null || quantity > item.getQuantity()){
            throw new GameActionException("You don't have enough " + template.getName());
        }
        else{
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

                buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                        client.getCharacter().getStatistics().getUsedPods(),
                        client.getCharacter().getStatistics().getMaxPods()
                ));
            }
        }
    }
}
