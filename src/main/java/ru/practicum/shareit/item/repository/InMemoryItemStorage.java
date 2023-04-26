package ru.practicum.shareit.item.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.util.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author MR.k0F31n
 */
@Component
@AllArgsConstructor
@Slf4j
public class InMemoryItemStorage implements ItemRepository {
    private final Map<Long, Item> items;

    @Override
    public List<ItemDto> findAllItem() {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items.values()) {
            itemsDto.add(RowMapper.toItemDto(item));
        }
        log.debug("Return Collection ItemDto, size collection: '{}'", itemsDto.size());
        return itemsDto;
    }

    @Override
    public ItemDto findItemById(Long id) {
        if (!checkItem(id)) {
            return null;
        }
        return RowMapper.toItemDto(items.get(id));
    }

    @Override
    public List<ItemDto> findAllItemByOwner(Long ownerId) {
        List<ItemDto> itemDtoByOwner = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getOwnerId().equals(ownerId)) {
                itemDtoByOwner.add(RowMapper.toItemDto(item));
            }
        }
        log.debug("Return Collection ItemDto by owner, size collection: '{}'", itemDtoByOwner.size());
        return itemDtoByOwner;
    }

    @Override
    public ItemDto createItem(Item item) {
        Long id = getId();
        item.setId(id);
        items.put(id, item);
        log.debug("Item added successfully, item info: '{}'", items.get(id));
        return RowMapper.toItemDto(items.get(id));
    }

    @Override
    public ItemDto updateItem(Item item, Long id) {
        item.setId(id);
        items.put(id, item);
        log.debug("Item update successfully, item after update info: '{}'", items.get(id));
        return RowMapper.toItemDto(items.get(id));
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

    private long getId() {
        long lastId = items.values().stream()
                .mapToLong(Item::getId)
                .max()
                .orElse(0);
        log.debug("Give a new id to the item: '{}'", lastId + 1);
        return lastId + 1;
    }
}
