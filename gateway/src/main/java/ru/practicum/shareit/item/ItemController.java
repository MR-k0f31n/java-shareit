package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemCollectionByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId,
                                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get collection items form user id={}", userId);
        return itemClient.getAllItemsCollectionByOwner(from, size, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long id) {
        log.info("Get item by id={}, user id={}", id, userId);
        return itemClient.getItemById(id, userId);
    }

    @PostMapping
    public ResponseEntity<Object> createNewItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @Valid @RequestBody ItemRequestDto itemDto) {
        log.info("Create new item {} from user id = {}", itemDto, ownerId);
        return itemClient.createNewItem(ownerId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemRequestDto itemDto, @PathVariable long id,
                                             @RequestHeader("X-Sharer-User-Id") long ownerId) {
        log.info("Update item id = {}, update {}, user id = {}", id, itemDto, ownerId);
        return itemClient.updateItem(itemDto, id, ownerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long id) {
        log.info("Try delete item id = {}, owner id = {}", id, ownerId);
        return itemClient.deleteItem(ownerId, id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                             @RequestParam String text) {
        log.info("Search item {}, Page from={}, size={}", text, from, size);
        return itemClient.searchItem(from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody @Valid CommentRequestDto commentInputDto) {
        log.info("Add comment {} from item id={}, user id={}", commentInputDto, itemId, userId);
        return itemClient.addComment(itemId, userId, commentInputDto);
    }
}
