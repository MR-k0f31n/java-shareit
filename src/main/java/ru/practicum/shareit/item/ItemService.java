package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentInputDto;

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
     * @param itemDto object
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto createNewItem(ItemDto itemDto, Long ownerId);

    /**
     * @param itemDto object
     * @param id      Long id item
     * @param ownerId Long id User
     * @return Item format Dto
     */
    ItemDto updateItem(ItemDto itemDto, Long id, Long ownerId);

    /**
     * @param id Long Item id
     * @return Item format Dto
     */
    ItemDto getItemDtoById(Long id);

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

    /**
     *
     * @param userId Long user ID from check user
     * @param itemId Long item ID from check item
     * @param commentInputDto input format object comment
     * @return return Item Format DTO witch all data (Item Info, Comments, Next/Last date booking)
     */
    CommentDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto);
}