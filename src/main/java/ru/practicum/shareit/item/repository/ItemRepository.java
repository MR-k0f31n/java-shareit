package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRepository {

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
     * @param id   Long id item
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

    /**
     * @param searchRequest free search request
     * @return collection Item format Dto
     */
    List<ItemDto> searchItem(Long ownerId, String searchRequest);
}
