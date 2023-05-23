package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingShortDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class ItemMapper {
    public static Item dtoToItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                new User());
    }

    public static ItemDto itemToDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                new BookingShortDto(),
                new BookingShortDto(),
                new ArrayList<>()
        );
    }

    public static List<ItemDto> itemToDto(Iterable<Item> items) {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        for (Item item : items) {
            itemsDtoList.add(itemToDto(item));
        }
        return itemsDtoList;
    }
}
