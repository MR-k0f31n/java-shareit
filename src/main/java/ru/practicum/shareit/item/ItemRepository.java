package ru.practicum.shareit.item;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRepository {

    /**
     * @param id Long Item id
     * @return Item object
     */
    Item findItemById(Long id);

    /**
     * @return All Item
     */
    List<Item> findAllItems();

    /**
     * @param item object
     * @return Item format Dto
     */
    Item createItem(Item item);

    /**
     * @param item object
     * @param id   Long id item
     * @return Item format Dto
     */
    Item updateItem(Item item, Long id);

    /**
     * @param id Long id item
     * @return boolean check Item in collection
     */
    boolean isItemExist(Long id);

    /**
     * @param id Long id item
     */
    void deleteItem(Long id);
}
