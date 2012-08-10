package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.common.client.protocol.enums.ItemPositionEnum;
import org.d2j.common.client.protocol.enums.SpellEffectsEnum;
import org.d2j.common.random.Dice;
import org.d2j.game.game.items.ItemEffect;
import org.d2j.game.game.spells.EffectFactory;
import org.d2j.game.game.spells.effects.Effect;
import org.d2j.game.game.spells.zones.SingleCell;
import org.d2j.game.model.*;
import org.d2j.game.model.Character;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IBaseEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: Blackrush
 * Date: 24/12/11
 * Time: 14:38
 * IDE : IntelliJ IDEA
 */
@Singleton
public class ItemRepository extends AbstractEntityRepository<Item, Long> {
    private final IEntityRepository<Character, Long> characters;
    private final IBaseEntityRepository<ItemTemplate, Integer> itemTemplates;

    private long nextId;

    @Inject
    public ItemRepository(@Dynamic EntitiesContext context, IEntityRepository<Character, Long> characters, IBaseEntityRepository<ItemTemplate, Integer> itemTemplates) {
        super(context);
        this.characters = characters;
        this.itemTemplates = itemTemplates;
    }

    @Override
    protected void setNextId(Item entity) {
        entity.setId(++nextId);
    }

    @Override
    protected String getCreateQuery(Item entity) {
        if (entity instanceof WeaponItem){
            return StringUtils.format(
                    "INSERT INTO `items`(`id`,`template`,`owner`,`effects`,`position`,`quantity`,`weaponEffects`) " +
                            "VALUES('{0}','{1}','{2}','{3}','{4}','{5}','{6}');",

                    entity.getId(),
                    entity.getTemplate().getId(),
                    entity.getOwner().getId(),
                    statsToString(entity.getEffects()),
                    entity.getPosition().value(),
                    entity.getQuantity(),
                    weaponEffectsToString(((WeaponItem) entity).getWeaponEffects())
            );
        }
        else{
            return StringUtils.format(
                    "INSERT INTO `items`(`id`,`template`,`owner`,`effects`,`position`,`quantity`,`weaponEffects`) " +
                            "VALUES('{0}','{1}','{2}','{3}','{4}','{5}','');",

                    entity.getId(),
                    entity.getTemplate().getId(),
                    entity.getOwner().getId(),
                    statsToString(entity.getEffects()),
                    entity.getPosition().value(),
                    entity.getQuantity()
            );
        }
    }

    @Override
    protected String getDeleteQuery(Item entity) {
        return "DELETE FROM `items` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getSaveQuery(Item entity) {
        if (entity instanceof WeaponItem){
            return StringUtils.format(
                    "UPDATE `items` SET " +
                            "`owner`='{0}', " +
                            "`effects`='{1}', " +
                            "`position`='{2}', " +
                            "`quantity`='{3}', " +
                            "`weaponEffects`='{4}' " +
                            "WHERE `id`='{5}';",

                    entity.getOwner().getId(),
                    statsToString(entity.getEffects()),
                    entity.getPosition().value(),
                    entity.getQuantity(),
                    weaponEffectsToString(((WeaponItem) entity).getWeaponEffects()),

                    entity.getId()
            );
        }
        else{
            return StringUtils.format(
                    "UPDATE `items` SET " +
                            "`owner`='{0}', " +
                            "`effects`='{1}', " +
                            "`position`='{2}', " +
                            "`quantity`='{3}' " +
                            "WHERE `id`='{4}';",

                    entity.getOwner().getId(),
                    statsToString(entity.getEffects()),
                    entity.getPosition().value(),
                    entity.getQuantity(),

                    entity.getId()
            );
        }
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `items`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `items` WHERE `id`='" + id + "';";
    }

    @Override
    protected Item loadOne(ResultSet reader) throws SQLException {
        ItemTemplate template = itemTemplates.findById(reader.getInt("template"));
        Item item;

        if (template instanceof WeaponItemTemplate){
            item = buildWeaponItem((WeaponItemTemplate)template, reader);
        }
        else{
            item = new Item(
                    reader.getLong("id"),
                    template,
                    characters.findById(reader.getLong("owner")),
                    buildStats(reader.getString("effects")),
                    ItemPositionEnum.valueOf(reader.getInt("position")),
                    reader.getInt("quantity")
            );
        }

        item.getOwner().getBag().put(item.getId(), item);
        if (item.isEquiped()){
            item.apply(item.getOwner().getStatistics());
        }

        return item;
    }

    private WeaponItem buildWeaponItem(WeaponItemTemplate template, ResultSet reader) throws SQLException {
        List<Effect> weaponEffects = new ArrayList<>(),
                     weaponEffectsCritic = new ArrayList<>();

        for (String effect : reader.getString("weaponEffects").split(";")){
            if (effect.isEmpty()) continue;

            String[] args = effect.split(",");
            SpellEffectsEnum e = SpellEffectsEnum.valueOf(Integer.parseInt(args[0], 16));
            Dice dice = Dice.parseDice(args[1]);

            Effect normal = EffectFactory.getInstance().get(e),
                   critic = EffectFactory.getInstance().get(e);

            normal.setDice(dice);
            critic.setDice(new Dice(dice).addAdd(template.getCriticalBonus()));

            weaponEffects.add(normal);
            weaponEffectsCritic.add(critic);
        }

        return new WeaponItem(
                reader.getLong("id"),
                template,
                characters.findById(reader.getLong("owner")),
                buildStats(reader.getString("effects")),
                ItemPositionEnum.valueOf(reader.getInt("position")),
                reader.getInt("quantity"),
                weaponEffects,
                new SingleCell(),
                weaponEffectsCritic,
                new SingleCell()
        );
    }

    private static Collection<ItemEffect> buildStats(String effects) {
        List<ItemEffect> result = new ArrayList<>();
        for (String effect : effects.split(";")){
            if (effect.isEmpty()) continue;
            result.add(ItemEffect.parseItemEffect(effect));
        }
        return result;
    }

    private static String statsToString(Collection<ItemEffect> effects){
        StringBuilder sb = new StringBuilder(5 * effects.size());
        boolean first = true;
        for (ItemEffect effect : effects){
            if (first) first = false;
            else sb.append(';');
            sb.append(effect.toString());
        }
        return sb.toString();
    }

    private static String weaponEffectsToString(Collection<Effect> weaponEffects){
        StringBuilder sb = new StringBuilder(5 * weaponEffects.size());
        boolean first = true;
        for (Effect effect : weaponEffects){
            if (first) first = false;
            else sb.append(';');

            sb.append(Integer.toString(effect.getEffect().value(), 16)).append(',');
            sb.append(effect.getDice().toString());
        }
        return sb.toString();
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!characters.isLoaded()){
            throw new LoadingException("CharacterRepository isn't loaded.");
        }

        if (!itemTemplates.isLoaded()){
            throw new LoadingException("ItemTemplateRepository isn't loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();

        for (Item item : entities.values()){
            if (nextId < item.getId()){
                nextId = item.getId();
            }
        }
    }
}
