package org.d2j.game.game.commands;

import org.d2j.common.NetworkStringBuffer;
import org.d2j.common.Permissions;
import org.d2j.common.client.protocol.InfoGameMessageFormatter;
import org.d2j.common.client.protocol.ItemGameMessageFormatter;
import org.d2j.game.game.logging.DofusLogger;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.game.repository.CharacterRepository;
import org.d2j.game.repository.ItemTemplateRepository;
import org.d2j.game.service.game.GameClient;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 09/01/12
 * Time: 18:58
 * To change this template use File | Settings | File Templates.
 */
public class GiveItemCommand extends AbstractCommand {
    private CharacterRepository characters;
    private ItemTemplateRepository itemTemplates;

    public GiveItemCommand(CharacterRepository characters, ItemTemplateRepository itemTemplates) {
        this.characters = characters;
        this.itemTemplates = itemTemplates;
    }

    @Override
    public String name() {
        return "item";
    }

    @Override
    public String helpMessage() {
        return "Give an item to the target";
    }

    @Override
    public String[] usageParameters(){
        return new String[]{
                "ID of Item",
                "QUANTITY:facultative",
                "ID or NAME of Character:facultative",
        };
    }

    @Override
    public Permissions level() {
        return Permissions.QUIZ_MASTER;
    }

    @Override
    public void parse(GameClient client, DofusLogger out, String[] args) {
        if (args.length < 1 || args.length > 3){
            out.error("Usage: " + usage());
        }
        else{
            ItemTemplate tpl = itemTemplates.findById(Integer.parseInt(args[0]));
            if (tpl == null){
                out.error("Unknown item {0}.", args[0]);
            }
            else{
                int quantity = args.length > 1 ? Integer.parseInt(args[1]) : 1;
                Character target = args.length > 2 ? characters.findByIdOrName(args[2]) : client.getCharacter();

                if (target == null){
                    out.error("Unknown target {0}.", args[1]);
                }
                else if (quantity < 1){
                    out.error("Invalid quantity {0}", args[2]);
                }
                else if (target.getBag().isFull()){
                    out.error("{0} has a full bag.", target.getName());
                }
                else{
                    Item item = tpl.generate(), same;
                    item.setOwner(target);
                    item.setQuantity(quantity);

                    if ((same = client.getCharacter().getBag().getSame(item)) != null){
                        same.addQuantity(quantity);
                    }
                    else{
                        client.getCharacter().getBag().add(item);
                    }

                    if (target.getOwner().isOnline()){
                        try (NetworkStringBuffer buf = new NetworkStringBuffer(target.getOwner().getClient().getSession())){
                            if (same != null){
                                buf.append(ItemGameMessageFormatter.quantityMessage(
                                        same.getId(),
                                        same.getQuantity()
                                ));
                            }
                            else{
                                buf.append(ItemGameMessageFormatter.addItemMessage(item.toBaseItemType()));
                            }

                            buf.append(ItemGameMessageFormatter.inventoryStatsMessage(
                                    target.getStatistics().getUsedPods(),
                                    target.getStatistics().getMaxPods()
                            ));
                        }

                        target.getOwner().getClient().getTchatOut().info(
                                "{0} gave you {1} * {2}.",
                                client.getCharacter().urlize(),
                                item.getTemplate().getName(),
                                quantity
                        );
                    }

                    out.info("You have successfully give {0} * {1} to {2}.",
                            tpl.getName(),
                            quantity,
                            target.urlize()
                    );
                }
            }
        }
    }
}
