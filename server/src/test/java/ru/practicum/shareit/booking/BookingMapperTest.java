package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingMapperTest {
    @Test
    void bookingToShortDto() {
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now(),
                new Item(1L, "null", "null", true, new User(), new ItemRequest()),
                new User(1L, "name", "222@mail.ru"), Status.WAITING);

        BookingShortDto bookingShortDto = BookingMapper.bookingToShortDto(booking);

        assertEquals(bookingShortDto.getItemId(), booking.getItem().getId());
    }
}
