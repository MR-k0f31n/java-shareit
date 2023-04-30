package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemService {

    /**
     * @param OwnerId Long Owner id
     * @return All Item format Dto by owner
     */
    List<ItemDto> getAllItemsByOwner(Long OwnerId);

    /**
     * @param item    object
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto createNewItem(Item item, Long ownerId);

    /**
     * @param item    object
     * @param id      Long id item
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto updateItem(Item item, Long id, Long ownerId);

    /**
     * @param id Long Item id
     * @return Item format Dto
     */
    ItemDto getItemById(Long id);

    /**
     * @param id      Long id item
     * @param ownerId Long id User
     */
    void deleteItem(Long id, Long ownerId);

    /**
     * @param searchRequest free search request
     * @param ownerId       Long id User
     * @return collection Item format Dto
     */
    List<ItemDto> searchItem(Long ownerId, String searchRequest);
}