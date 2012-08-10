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
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public class AddItemAction implements ILiveAction {
    private final ItemTemplate template;
    private final int quantity;

    public AddItemAction(ItemTemplate template, int quantity) {
        this.template = template;
        this.quantity = quantity;
    }

    @Override
    public LiveActionType getResponseType() {
        return LiveActionType.ADD_ITEM;
    }

    @Override
    public void apply(GameClient client) throws GameActionException {
        Item item = template.generate();
        item.setOwner(client.getCharacter());
        item.setQuantity(quantity);

        Item same;

        try (NetworkStringBuffer buf = new NetworkStringBuffer(client.getSession())){
            if ((same = client.getCharacter().getBag().getSame(item)) != null){
                same.addQuantity(quantity);
                buf.append(ItemGameMessageFormatter.quantityMessage(
                        same.getId(),
                        same.getQuantity()
                ));
            }
            else{
                client.getCharacter().getBag().add(item);
                buf.append(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
            }

            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                    client.getCharacter().getStatistics().getUsedPods(),
                    client.getCharacter().getStatistics().getMaxPods()
            ));
        }
    }
}
