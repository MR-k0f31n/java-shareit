package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.*;

/**
 * @author MR.k0F31n
 */
@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private ItemRepository repository;
    private UserService userService;

    @Override
    public List<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Task get all items by owner");
        userService.findUserById(id);
        return toItemDtoList(repository.getItemsByOwnerId(id));
    }

    @Override
    public ItemDto createNewItem(ItemDto itemDto, Long ownerId) {
        log.warn("Task create new item, item info: '{}'", itemDto);
        userService.findUserById(ownerId);
        final Item item = toItem(itemDto, ownerId);
        item.getOwner().setId(ownerId);
        return toItemDto(repository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long id, Long ownerId) {
        log.warn("Task update item, item info: " + itemDto);
        userService.findUserById(ownerId);
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name == null && description == null && available == null) {
            return null;
        }
        final Item itemForUpdate = repository.findById(id).orElseThrow(
                ()->new NotFoundException("Item not found! Item id: " + id));
        if (!itemForUpdate.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        if (name != null && !name.isBlank()) {
            itemForUpdate.setName(name);
        }
        if (description != null && !description.isBlank()) {
            itemForUpdate.setDescription(description);
        }
        if (available != null) {
            itemForUpdate.setAvailable(available);
        }
        return toItemDto(repository.save(itemForUpdate));
    }

    @Override
    public ItemDto getItemById(Long id) {
        log.warn("Task get item by id, item id: '{}'", id);
        Item item = repository.findById(id).orElseThrow(
                ()->new NotFoundException("Item not found! Item id: " + id));
        return toItemDto(item);
    }

    @Override
    public void deleteItem(Long id, Long ownerId) {
        userService.findUserById(ownerId);
        if (!getItemById(id).getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        log.warn("try delete item by id, item id: '{}'", id);
        repository.deleteById(id);
    }

    @Override
    public List<ItemDto> searchItem(String requestSearch) {
        if (requestSearch.isEmpty() || requestSearch.isBlank()) {
            return Collections.emptyList();
        }
        return toItemDtoList(repository.searchItems(requestSearch));
    }
}
