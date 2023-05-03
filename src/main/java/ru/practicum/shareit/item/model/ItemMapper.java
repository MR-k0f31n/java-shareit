package ru.practicum.shareit.item.model;

import ru.practicum.shareit.item.dto.ItemDto;

/**
 * @author MR.k0F31n
 */
public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getOwnerId(),
                itemDto.getRequest());
    }
}
