package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;


import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.item.dto.ItemDtoMapper.toItemDto;
import static ru.practicum.shareit.item.model.ItemMapper.toItem;

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
        return toItemDto(repository.createItem(toItem(itemDto)));
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
        if (!repositoryUser.isUserExist(ownerId)) {
            throw new NotFoundException("User not found! User id: " + ownerId);
        }
        if (!getItemById(id).getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        log.warn("try delete item by id, item id: '{}'", id);
        repository.deleteItem(id);
    }

    @Override
    public List<ItemDto> searchItem(Long ownerId, String requestSearch) {
        if (!repositoryUser.isUserExist(ownerId)) {
            throw new NotFoundException("User not found! User id: " + ownerId);
        }
        if (requestSearch.isEmpty()) {
            return new ArrayList<>();
        }
        return repository.searchItem(ownerId, requestSearch);
    }
}
