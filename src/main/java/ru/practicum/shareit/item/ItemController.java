/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

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
        return service.getItemById(id);
    }

    @PostMapping
    public ItemDto createNewItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @Valid @RequestBody Item item) {
        log.debug("Endpoint request: 'POST /items'");
        return service.createNewItem(item, ownerId);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@RequestBody Item item, @PathVariable Long id,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.debug("Endpoint request: 'PUT /items/{id}'");
        return service.updateItem(item, id, ownerId);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@RequestHeader("X-Sharer-User-Id") Long ownerId, @PathVariable Long id) {
        log.debug("Endpoint request: 'DELETE /items/{id}'");
        service.deleteItem(ownerId, id);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                    @RequestParam String text) {
        log.debug("Endpoint request: 'GET /items/{search}'");
        return service.searchItem(ownerId, text);
    }
}
