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
                new User(),
                null
        );
    }

    public static Item dtoToItem(ItemInputDto dto) {
        return new Item(
                null,
                dto.getName(),
                dto.getDescription(),
                dto.getAvailable(),
                new User(),
                dto.getRequestId()
        );
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
                new ArrayList<>(),
                item.getRequestId()
        );
    }

    public static ItemShortDto itemToShortDto(Item item) {
        return new ItemShortDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getRequestId()
        );
    }

    public static List<ItemDto> itemToDto(Iterable<Item> items) {
        List<ItemDto> itemsDtoList = new ArrayList<>();
        for (Item item : items) {
            itemsDtoList.add(itemToDto(item));
        }
        return itemsDtoList;
    }

    public static List<ItemShortDto> itemToShortDtoList(Iterable<Item> items) {
        List<ItemShortDto> itemsDtoList = new ArrayList<>();
        for (Item item : items) {
            itemsDtoList.add(itemToShortDto(item));
        }
        return itemsDtoList;
    }
}
