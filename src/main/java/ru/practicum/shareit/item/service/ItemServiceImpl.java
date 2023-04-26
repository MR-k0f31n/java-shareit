package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

/**
 * @author MR.k0F31n
 */
@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private ItemRepository repository;
    private UserRepository repositoryUser;

    @Override
    public List<ItemDto> getAllItems() {
        log.debug("Task get all items");
        return repository.findAllItem();
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Task get all items by owner");
        if (!repositoryUser.checkUser(id)) {
            throw new NotFoundException("User not found! User id: " + id);
        }
        return repository.findAllItemByOwner(id);
    }

    @Override
    public ItemDto createNewItem(Item item) {
        log.warn("Task create new item, item info: '{}'", item);
        if (!repositoryUser.checkUser(item.getOwnerId())) {
            throw new NotFoundException("User not found! User id: " + item.getOwnerId());
        }
        return repository.createItem(item);
    }

    @Override
    public ItemDto updateItem(Item item, Long id) {
        log.warn("Task update item, item info: " + item);
        if (!repositoryUser.checkUser(item.getOwnerId())) {
            throw new NotFoundException("User not found! User id: " + item.getOwnerId());
        }
        if (!repository.checkItem(id)) {
            throw new NotFoundException("Item not found! Item id: " + id);
        }
        return repository.updateItem(item, id);
    }

    @Override
    public ItemDto getItemById(Long id) {
        log.warn("Task get item by id, item id: '{}'", id);
        if (!repository.checkItem(id)) {
            throw new NotFoundException("Item not found! Item id: " + id);
        }
        return repository.findItemById(id);
    }

    @Override
    public void deleteItem(Long id) {
        log.warn("try delete item by id, item id: '{}'", id);
        repository.deleteItem(id);
    }
}
