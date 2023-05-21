/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentInputDto;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
@AllArgsConstructor
@Slf4j
public class ItemController {
    private ItemService service;

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Endpoint request: 'GET /items'");
        return service.getAllItemsByOwner(userId);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        log.debug("Endpoint request: 'GET /items/{id}'");
        return service.getItemDtoById(id);
    }

    @PostMapping
    public ItemDto createNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemDto itemDto) {
        log.debug("Endpoint request: 'POST /items'");
        return service.createNewItem(itemDto, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto, @PathVariable Long id,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.debug("Endpoint request: 'PUT /items/{id}'");
        return service.updateItem(itemDto, id, ownerId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long id) {
        log.debug("Endpoint request: 'DELETE /items/{id}'");
        service.deleteItem(ownerId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        log.debug("Endpoint request: 'GET items/search?text='" + text);
        return service.searchItem(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentInputDto commentInputDto) {
        return service.addComment(userId, itemId, commentInputDto);
    }
}
