package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemService {

    /**
     * @return All Item format Dto
     */
    List<ItemDto> getAllItems();

    /**
     * @param id Long Owner id
     * @return All Item format Dto by owner
     */
    List<ItemDto> getAllItemsByOwner(Long id);

    /**
     * @param item object
     * @return Item format Dto
     */
    ItemDto createNewItem(Item item);

    /**
     * @param item object
     * @param id Long id item
     * @return Item format Dto
     */
    ItemDto updateItem(Item item, Long id);

    /**
     * @param id Long Item id
     * @return Item format Dto
     */
    ItemDto getItemById(Long id);

    /**
     * @param id Long id item
     */
    void deleteItem (Long id);
}
