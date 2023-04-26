package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.util.RowMapper.toItemDto;

/**
 * @author MR.k0F31n
 */
@Component
@Slf4j
public class InMemoryItemStorage implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currencyId = 1L;

    @Override
    public ItemDto findItemById(Long id) {
        if (!checkItem(id)) {
            return null;
        }
        log.debug("Return ItemDto, item info: '{}'", toItemDto(items.get(id)));
        return toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> findAllItemByOwner(Long ownerId) {
        List<ItemDto> itemDtoByOwner = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId().equals(ownerId)) {
                itemDtoByOwner.add(toItemDto(item));
            }
        }
        log.debug("Return Collection ItemDto by owner, size collection: '{}'", itemDtoByOwner.size());
        return itemDtoByOwner;
    }

    @Override
    public ItemDto createItem(Item item) {
        Long id = currencyId++;
        item.setId(id);
        items.put(id, item);
        log.debug("Item added successfully, item info: '{}'", toItemDto(items.get(id)));
        return toItemDto(items.get(id));
    }

    @Override
    public ItemDto updateItem(Item item, Long id) {
        Item forUpdateItem = items.get(id);
        Boolean available = item.getAvailable();
        String name = item.getName();
        String description = item.getDescription();
        if (name != null) {
            forUpdateItem.setName(name);
        }
        if (description != null) {
            forUpdateItem.setDescription(description);
        }
        if (available != null) {
            forUpdateItem.setAvailable(available);
        }
        items.put(id, forUpdateItem);
        log.debug("Item update successfully, item after update info: '{}'", toItemDto(items.get(id)));
        return toItemDto(items.get(id));
    }

    @Override
    public boolean checkItem(Long id) {
        if (items.containsKey(id)) {
            log.debug("Item detected id '{}'", id);
            return true;
        }
        log.debug("Item not Found id '{}'", id);
        return false;
    }

    @Override
    public void deleteItem(Long id) {
        items.remove(id);
        log.debug("User delete successfully, check user: '{}'", checkItem(id));
    }

    @Override
    public List<ItemDto> searchItem(Long ownerId, String searchRequest) {
        List<ItemDto> found = new ArrayList<>();
        String requestFormat = searchRequest.toLowerCase();
        for (Item item : items.values()) {
            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(requestFormat) ||
                        item.getDescription().toLowerCase().contains(requestFormat)) {
                    found.add(toItemDto(item));
                }
            }
        }
        log.debug("Found count item: '{}'", found.size());
        return found;
    }
}