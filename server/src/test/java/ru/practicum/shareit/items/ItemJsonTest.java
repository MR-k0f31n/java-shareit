package ru.practicum.shareit.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingShortDto;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author MR.k0F31n
 */
@JsonTest
public class ItemJsonTest {
    @Autowired
    private JacksonTester<ItemDto> jsonItemDto;

    @Test
    void testItemDto() throws Exception {
        ItemDto itemDto = new ItemDto(1L, "Отвертка", "Коктель", true,
                new User(1L, "Name1", "email@mail.ru"), new BookingShortDto(), new BookingShortDto(),
                new ArrayList<>(), null);

        JsonContent<ItemDto> result = jsonItemDto.write(itemDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("Коктель");
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("Отвертка");
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isNull();
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathStringValue("$.owner.name").isEqualTo("Name1");
        assertThat(result).extractingJsonPathStringValue("$.owner.email").isEqualTo("email@mail.ru");
    }
}
