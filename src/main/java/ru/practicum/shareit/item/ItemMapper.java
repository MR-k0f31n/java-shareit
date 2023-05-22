package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingItemBookerDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class ItemMapper {
    public static Item toItem(ItemDto itemDto, Long ownerId) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                new User());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner(),
                new BookingItemBookerDto(),
                new BookingItemBookerDto(),
                new ArrayList<>()
        );
    }

    public static List<ItemDto> toItemDtoList(Iterable<Item> items) {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        for (Item item : items) {
            itemsDtoList.add(toItemDto(item));
        }
        return itemsDtoList;
    }
}
