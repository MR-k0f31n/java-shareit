package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author MR.k0F31n
 */
@Component
@Slf4j
public class InMemoryItemStorage implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long currencyId = 1L;

    @Override
    public Item findItemById(Long id) {
        log.debug("Return Item, item info: '{}'", items.get(id));
        return items.get(id);
    }

    @Override
    public List<Item> findAllItems() {
         log.debug("Return Collection ItemDto by owner, size collection: '{}'", items.size());
        return List.copyOf(items.values());
    }

    @Override
    public Item createItem(Item item) {
        Long id = currencyId++;
        item.setId(id);
        items.put(id, item);
        log.debug("Item added successfully, item info: '{}'", items.get(id));
        return items.get(id);
    }

    @Override
    public Item updateItem(Item item, Long id) {
        items.put(id, item);
        log.debug("Item update successfully, item after update info: '{}'", items.get(id));
        return items.get(id);
    }

    @Override
    public boolean isItemExist(Long id) {
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
        log.debug("User delete successfully, check user: '{}'", isItemExist(id));
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
