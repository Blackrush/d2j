package org.d2j.game.repository;

import org.d2j.common.client.protocol.enums.ItemEffectEnum;
import org.d2j.common.client.protocol.enums.ItemTypeEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.items.ItemEffectTemplate;
import org.d2j.game.game.live_actions.ILiveAction;
import org.d2j.game.game.live_actions.LazyLoadLiveAction;
import org.d2j.game.game.live_actions.LiveActionType;
import org.d2j.game.game.spells.EffectFactory;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.model.ItemSetTemplate;
import org.d2j.game.model.ItemTemplate;
import org.d2j.game.model.UsableItemTemplate;
import org.d2j.game.model.WeaponItemTemplate;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Static;
import org.d2j.utils.database.repository.AbstractBaseEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.d2j.common.StringUtils.fromHex;

/**
 * User: Blackrush
 * Date: 23/12/11
 * Time: 19:55
 * IDE : IntelliJ IDEA
 */
@Singleton
public class ItemTemplateRepository extends AbstractBaseEntityRepository<ItemTemplate, Integer> {
    private final IBaseEntityRepository<ItemSetTemplate, Short> itemSetTemplates;

    @Inject
    public ItemTemplateRepository(@Static EntitiesContext context, IBaseEntityRepository<ItemSetTemplate, Short> itemSetTemplates) {
        super(context);
        this.itemSetTemplates = itemSetTemplates;
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!itemSetTemplates.isLoaded()){
            throw new LoadingException("ItemSetRepository isn't loaded.");
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `item_templates`;";
    }

    @Override
    protected String getLoadOneQuery(Integer id) {
        return "SELECT * FROM `item_templates` WHERE `id`='" + id + "';";
    }

    @Override
    protected ItemTemplate loadOne(ResultSet reader) throws SQLException {
        ItemTemplate tpl;
        if (!reader.getString("weaponInfo").isEmpty()){
            tpl = buildWeapon(reader);
        }
        else if (reader.getBoolean("usable")){
            tpl = buildUsableItem(reader);
        }
        else{
            tpl = buildItem(reader);
        }

        if (tpl.getItemSet() != null){
            tpl.getItemSet().getItems().add(tpl);
        }

        return tpl;
    }

    private WeaponItemTemplate buildWeapon(ResultSet reader) throws SQLException {
        Collection<ItemEffectTemplate> effects = buildItemStats(reader.getString("stats"));
        String[] weaponArgs = reader.getString("weaponInfo").split(",");
        short criticalBonus = Short.parseShort(weaponArgs[0]);

        List<Effect> weaponEffects       = new ArrayList<>(),
                     weaponEffectsCritic = new ArrayList<>();

        for (ItemEffectTemplate effect : effects){
            SpellEffectsEnum spellEffect = SpellEffectsEnum.fromItemEffect(effect.getEffect());
            if (spellEffect != null){
                Effect normal = EffectFactory.getInstance().get(spellEffect),
                       critic = EffectFactory.getInstance().get(spellEffect);

                normal.setDice(new Dice(effect.getDice()));
                critic.setDice(new Dice(effect.getDice()).addAdd(criticalBonus));

                weaponEffects.add(normal);
                weaponEffectsCritic.add(critic);
            }
        }

        return new WeaponItemTemplate(
                reader.getInt("id"),
                reader.getString("name"),
                itemSetTemplates.findById(reader.getShort("itemSet")),
                ItemTypeEnum.valueOf(reader.getInt("type")),
                reader.getShort("level"),
                reader.getInt("weight"),
                reader.getBoolean("forgemageable"),
                reader.getInt("price"),
                reader.getString("conditions"),
                effects,
                reader.getBoolean("twoHands"),
                reader.getBoolean("isEthereal"),
                criticalBonus,
                Short.parseShort(weaponArgs[1]),
                Short.parseShort(weaponArgs[2]),
                Short.parseShort(weaponArgs[3]),
                Short.parseShort(weaponArgs[4]),
                Short.parseShort(weaponArgs[5]),
                weaponEffects,
                weaponEffectsCritic
        );
    }

    private UsableItemTemplate buildUsableItem(ResultSet reader) throws SQLException {
        return new UsableItemTemplate(
                reader.getInt("id"),
                reader.getString("name"),
                itemSetTemplates.findById(reader.getShort("itemSet")),
                ItemTypeEnum.valueOf(reader.getInt("type")),
                reader.getShort("level"),
                reader.getInt("weight"),
                reader.getBoolean("forgemageable"),
                reader.getInt("price"),
                reader.getString("conditions"),
                buildItemStats(reader.getString("stats")),
                buildEffects(reader.getString("useEffects")),
                reader.getBoolean("targetable")
        );
    }

    private ItemTemplate buildItem(ResultSet reader) throws SQLException {
        return new ItemTemplate(
                reader.getInt("id"),
                reader.getString("name"),
                itemSetTemplates.findById(reader.getShort("itemSet")),
                ItemTypeEnum.valueOf(reader.getInt("type")),
                reader.getShort("level"),
                reader.getInt("weight"),
                reader.getBoolean("forgemageable"),
                reader.getInt("price"),
                reader.getString("conditions"),
                buildItemStats(reader.getString("stats"))
        );
    }

    private static Collection<ItemEffectTemplate> buildItemStats(String stats){
        List<ItemEffectTemplate> result = new ArrayList<>();

        for (String stat : stats.split(",")) if (!stat.isEmpty()) {
            String[] effects = stat.split("#");

            result.add(new ItemEffectTemplate(
                    ItemEffectEnum.valueOf(fromHex(effects[0])),
                    effects.length > 4 ? Dice.parseDice(effects[4]) : Dice.EMPTY_DICE
            ));
        }

        return result;
    }

    private static Collection<ILiveAction> buildEffects(String pEffects){
        /*
           * Mise en forme :
           * La liste :          {partie0}|{partie1}|...|{partieN}
           * Une partie :        {type};{arg0},{arg1},...,{argN}
           * Un argument :       voir selon {type}
         */

        String[] effects = pEffects.split("\\|");
        List<ILiveAction> actions = new ArrayList<>(effects.length);
        for (String effect : effects){
            if (effect.isEmpty()) continue;

            String[] args = effect.split(";");
            LiveActionType type = LiveActionType.valueOf(Integer.parseInt(args[0]));
            if (type == LiveActionType.NONE) continue;

            actions.add(new LazyLoadLiveAction(type, args[1].split(",")));
        }
        return actions;
    }
}
