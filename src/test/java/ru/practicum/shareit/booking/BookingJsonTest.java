package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author MR.k0F31n
 */
@JsonTest
public class BookingJsonTest {
    @Autowired
    private JacksonTester<BookingDto> jsonBookingDto;

    @Autowired
    private JacksonTester<BookingShortDto> jsonBookingShortDto;

    @Test
    void testBookingDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 1, 20, 12, 10, 30);
        LocalDateTime end = LocalDateTime.of(2025, 2, 20, 12, 10, 30);

        BookingDto bookingDto = new BookingDto(1L, start, end, 1L, new Item(), new User(), Status.APPROVED);

        JsonContent<BookingDto> result = jsonBookingDto.write(bookingDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(Status.APPROVED.toString());
    }

    @Test
    void testBookingShortDto() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 1, 20, 12, 10, 30);
        LocalDateTime end = LocalDateTime.of(2025, 2, 20, 12, 10, 30);

        BookingShortDto bookingShortDto = new BookingShortDto(1L, 1L, 1L, start, end);

        JsonContent<BookingShortDto> result = jsonBookingShortDto.write(bookingShortDto);

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
    }
}
