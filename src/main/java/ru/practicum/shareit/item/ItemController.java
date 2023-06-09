/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentInputDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/items")
@AllArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private ItemService service;

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                            @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                            @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Endpoint request: 'GET /items'");
        final Pageable pageable = PageRequest.of(from / size, size);
        return service.getAllItemsByOwner(userId, pageable);
    }

    @GetMapping("/{id}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long id) {
        log.debug("Endpoint request: 'GET /items/{id}'");
        return service.getItemDtoById(id, userId);
    }

    @PostMapping
    public ItemDto createNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody ItemInputDto itemDto) {
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
    public List<ItemDto> searchItem(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                    @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                    @RequestParam String text) {
        log.debug("Endpoint request: 'GET items/search?text='" + text);
        final Pageable pageable = PageRequest.of(from / size, size);
        return service.searchItem(text, pageable);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long itemId,
                                 @RequestBody @Valid CommentInputDto commentInputDto) {
        log.debug("Endpoint request: 'GET items/{itemId}/comment = '" + commentInputDto.getText());
        return service.addComment(userId, itemId, commentInputDto);
    }
}
