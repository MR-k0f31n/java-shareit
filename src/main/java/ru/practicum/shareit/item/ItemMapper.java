package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.Booking;
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
                new Booking(),
                new Booking(),
                new ArrayList<>()
                );
    }

    public static List<ItemDto> toItemDtoList(Iterable<Item> items) {
        List<ItemDto> ItemsDtoList = new ArrayList<>();
        for (Item item : items) {
            ItemsDtoList.add(toItemDto(item));
        }
        return ItemsDtoList;
    }
}
