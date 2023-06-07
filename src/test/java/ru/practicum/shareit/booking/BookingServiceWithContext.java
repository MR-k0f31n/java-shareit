package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceWithContext {

    private final BookingService bookingService;
    private final ItemService itemService;
    private final UserService userService;
    private UserDto owner;
    private UserDto booker;
    private ItemDto item;
    private BookingDto booking;

    @BeforeEach
    void beforeEach() {

        owner = userService.createNewUser(new UserDto(null, "name1", "email@mail.com"));
        booker = userService.createNewUser(new UserDto(null, "name2", "email2@mail.ru"));
        item = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая", true,
                null), owner.getId());
        booking = bookingService.createNewBooking(new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusMinutes(2), item.getId()), booker.getId());
    }

    @DirtiesContext
    @Test
    public void create_returnsTheCorrectBookingDto_underNormalConditions() {

        assertEquals(booking.getId(), 1);
        assertEquals(booking.getStatus(), Status.WAITING);
        assertEquals(booking.getBooker().getId(), booker.getId());
        assertEquals(booking.getBooker().getName(), booker.getName());
        assertEquals(booking.getItem().getId(), item.getId());
        assertEquals(booking.getItem().getName(), item.getName());
    }

    @DirtiesContext
    @Test
    public void create_returnsException_bookYourItems() {

        ItemDto newItemDto = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая с батарейкой",
                true, null), owner.getId());
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusMinutes(2), newItemDto.getId());

        assertThrows(NotFoundException.class, () -> bookingService.createNewBooking(newBooking, owner.getId()));
    }
}
