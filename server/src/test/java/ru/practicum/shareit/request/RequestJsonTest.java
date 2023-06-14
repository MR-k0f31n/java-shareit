package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class RequestJsonTest {
    @Autowired
    private JacksonTester<ItemRequestDto> jsonItemDto;

    @Test
    void testItemDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2025, 2, 20, 12, 10, 30);
        ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "need item", 1L, created);

        JsonContent<ItemRequestDto> result = jsonItemDto.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("need item");
        assertThat(result).extractingJsonPathNumberValue("$.requesterId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
    }
}
