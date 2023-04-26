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
    public List<ItemDto> getAllItems() {
        log.debug("Endpoint request: 'GET /items'");
        return service.getAllItems();
    }

    /*@GetMapping("/{id}")
    public List<ItemDto> getAllItemsByOwner(@PathVariable Long id) {
        log.debug("Endpoint request: 'GET /items/{id}'");
        return service.getAllItemsByOwner(id);
    }*/

    @GetMapping("/{id}")
    public ItemDto getItemById(@PathVariable Long id) {
        log.debug("Endpoint request: 'GET /items/{id}'");
        return service.getItemById(id);
    }

    @PostMapping
    public ItemDto createNewItem(@Valid @RequestBody Item item) {
        log.debug("Endpoint request: 'POST /items'");
        return service.createNewItem(item);
    }

    @PatchMapping("/{id}")
    public ItemDto updateItem(@Valid @RequestBody Item item, @PathVariable Long id) {
        log.debug("Endpoint request: 'PUT /items/{id}'");
        return service.updateItem(item, id);
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable Long id) {
        log.debug("Endpoint request: 'DELETE /items/{id}'");
        service.deleteItem(id);
    }
}
