package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemService {

    /**
     * @param ownerId Long Owner id
     * @return All Item format Dto by owner
     */
    List<ItemDto> getAllItemsByOwner(Long ownerId);

    /**
     * @param ItemDto object
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto createNewItem(ItemDto ItemDto, Long ownerId);

    /**
     * @param ItemDto object
     * @param id      Long id item
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto updateItem(ItemDto ItemDto, Long id, Long ownerId);

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
     * @return collection Item format Dto
     */
    List<ItemDto> searchItem(String searchRequest);
}