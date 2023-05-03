package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;

/**
 * @author MR.k0F31n
 */
public class ItemDtoMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwnerId(),
                item.getRequest());
    }
}
