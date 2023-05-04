package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.ItemDtoMapper.toItem;
import static ru.practicum.shareit.item.ItemDtoMapper.toItemDto;

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
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : repository.findAllItems()) {
            if (item.getOwnerId().equals(id)) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }

    @Override
    public ItemDto createNewItem(ItemDto itemDto, Long ownerId) {
        log.warn("Task create new item, item info: '{}'", itemDto);
        userService.findUserById(ownerId);
        itemDto.setOwnerId(ownerId);
        Item item = toItem(itemDto);
        return toItemDto(repository.createItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long id, Long ownerId) {
        log.warn("Task update item, item info: " + itemDto);
        userService.findUserById(ownerId);
        Item itemForUpdate = toItem(getItemById(id));
        if (!itemForUpdate.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name != null) {
            itemForUpdate.setName(name);
        }
        if (description != null) {
            itemForUpdate.setDescription(description);
        }
        if (available != null) {
            itemForUpdate.setAvailable(available);
        }
        return toItemDto(repository.updateItem(itemForUpdate, id));
    }

    @Override
    public ItemDto getItemById(Long id) {
        log.warn("Task get item by id, item id: '{}'", id);
        try {
            return toItemDto(repository.findItemById(id));
        } catch (NullPointerException exception) {
            throw new NotFoundException("Item not found! Item id: " + id);
        }
    }

    @Override
    public void deleteItem(Long id, Long ownerId) {
        userService.findUserById(ownerId);
        if (!getItemById(id).getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        log.warn("try delete item by id, item id: '{}'", id);
        repository.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItem(String requestSearch) {
        log.debug("Searched items: '{}', searched items to lower case: '{}'", requestSearch, requestSearch.toLowerCase());
        List<ItemDto> findItems = new ArrayList<>();
        if (requestSearch.isEmpty()) {
            log.debug("Request Search empty");
            return findItems;
        }
        String requestSearchLowerFormat = requestSearch.toLowerCase();
        for (Item item : repository.findAllItems()) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(requestSearchLowerFormat)
                        || item.getDescription().toLowerCase().contains(requestSearchLowerFormat)) {
                    findItems.add(toItemDto(item));
                }
            }
        }
        log.debug("Find items: '{}'", findItems.size());
        return findItems;
    }
}
