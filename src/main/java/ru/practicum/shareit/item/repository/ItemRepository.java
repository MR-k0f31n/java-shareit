/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    /**
     * @return All Item format Dto
     */
    List<ItemDto> findAllItem();

    /**
     * @param id Long Item id
     * @return Item format Dto
     */
    ItemDto findItemById(Long id);

    /**
     * @param ownerId Long Owner id
     * @return All Item format Dto by owner
     */
    List<ItemDto> findAllItemByOwner(Long ownerId);

    /**
     * @param item object
     * @return Item format Dto
     */
    ItemDto createItem(Item item);

    /**
     * @param item object
     * @param id Long id item
     * @return Item format Dto
     */
    ItemDto updateItem(Item item, Long id);

    /**
     * @param id Long id item
     * @return boolean check Item in collection
     */
    boolean checkItem(Long id);

    /**
     * @param id Long id item
     */
    void deleteItem(Long id);
}
