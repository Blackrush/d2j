package org.d2j.game.repository;

import org.d2j.common.StringUtils;
import org.d2j.game.model.Item;
import org.d2j.game.model.StoredItem;
import org.d2j.utils.Action1;
import org.d2j.utils.database.EntitiesContext;
import org.d2j.utils.database.LoadingException;
import org.d2j.utils.database.annotation.Dynamic;
import org.d2j.utils.database.repository.AbstractEntityRepository;
import org.d2j.utils.database.repository.IEntityRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: 23/02/12
 * Time: 19:39
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class StoredItemRepository extends AbstractEntityRepository<StoredItem, Long> implements Action1<Item> {
    private final IEntityRepository<Item, Long> items;

    @Inject
    protected StoredItemRepository(@Dynamic EntitiesContext context, IEntityRepository<Item, Long> items) {
        super(context);
        this.items = items;
        this.items.addListenerOnDeleted(this);
    }

    @Override
    protected void beforeLoading() throws LoadingException {
        super.beforeLoading();

        if (!items.isLoaded()){
            throw new LoadingException("ItemRepository is not loaded.");
        }
    }

    @Override
    protected void afterLoading() {
        super.afterLoading();
    }

    @Override
    public void delete(StoredItem entity) {
        super.delete(entity);

        if (entity.getItem() != null && entity.getItem().getQuantity() <= 0){
            items.delete(entity.getId());
        }
    }

    @Override
    protected void setNextId(StoredItem entity) {
    }

    @Override
    protected String getCreateQuery(StoredItem entity) {
        return StringUtils.format("INSERT INTO `stored_items`(`id`,`quantity`,`price`) VALUES('{0}','{1}','{2}');",
                entity.getId(),
                entity.getQuantity(),
                entity.getPrice()
        );
    }

    @Override
    protected String getDeleteQuery(StoredItem entity) {
        return StringUtils.format("UPDATE `stored_items` SET `quantity`='{0}', `price`='{1}' WHERE `id`='{2}';",
                entity.getQuantity(),
                entity.getPrice(),
                entity.getId()
        );
    }

    @Override
    protected String getSaveQuery(StoredItem entity) {
        return "DELETE FROM `stored_items` WHERE `id`='" + entity.getId() + "';";
    }

    @Override
    protected String getLoadQuery() {
        return "SELECT * FROM `stored_items`;";
    }

    @Override
    protected String getLoadOneQuery(Long id) {
        return "SELECT * FROM `stored_items`WHERE `id`='" + id + "';";
    }

    @Override
    protected StoredItem loadOne(ResultSet reader) throws SQLException {
        Item item = items.findById(reader.getLong("id"));
        if (item != null){
            StoredItem sItem = new StoredItem(
                    items.findById(reader.getLong("id")),
                    reader.getInt("quantity"),
                    reader.getLong("price")
            );
            item.getOwner().getStore().addEntity(sItem);
            return sItem;
        }
        return null;
    }

    @Override
    public void call(Item obj) {
        StoredItem sItem = findById(obj.getId());
        if (sItem != null){
            sItem.setItem(null);
            delete(sItem);
        }
    }
}
