package ru.practicum.shareit.items;


import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {
    @Test
    void dtoToItem() {
        ItemDto itemDto = new ItemDto(1L, "nam", "des", true,
                new User(1L, "nam", "nam@nam.ru"), null, null, new ArrayList<>(), null);

        Item item = ItemMapper.dtoToItem(itemDto);

        assertEquals(itemDto.getName(), item.getName());
    }
}
