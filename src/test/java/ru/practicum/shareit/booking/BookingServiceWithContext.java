package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatus;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        booking = bookingService.createNewBooking(new BookingInputDto(LocalDateTime.now().plusSeconds(1),
                LocalDateTime.now().plusSeconds(3), item.getId()), booker.getId());
    }

    @DirtiesContext
    @Test
    void checkCreateNewItemUserAndBooking() {
        assertEquals(booking.getId(), 1);
        assertEquals(booking.getStatus(), Status.WAITING);
        assertEquals(booking.getBooker().getId(), booker.getId());
        assertEquals(booking.getBooker().getName(), booker.getName());
        assertEquals(booking.getItem().getId(), item.getId());
        assertEquals(booking.getItem().getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void createNewBooking_tryBookingItemOwned_expectedError() {
        ItemDto newItemDto = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая с батарейкой",
                true, null), owner.getId());
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusMinutes(2), newItemDto.getId());

        assertThrows(NotFoundException.class, () -> bookingService.createNewBooking(newBooking, owner.getId()));
    }

    @DirtiesContext
    @Test
    void createNewBooking_endDateBookingNotCorrect_expectedError() {
        ItemDto newItemDto = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая с батарейкой",
                true, null), owner.getId());
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusSeconds(3), newItemDto.getId());

        assertThrows(ValidatorException.class, () -> bookingService.createNewBooking(newBooking, booker.getId()));
    }

    @DirtiesContext
    @Test
    void createNewBooking_staartDateBookingNotCorrect_expectedError() {
        ItemDto newItemDto = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая с батарейкой",
                true, null), owner.getId());
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().minusMinutes(2), newItemDto.getId());

        assertThrows(ValidatorException.class, () -> bookingService.createNewBooking(newBooking, booker.getId()));
    }

    @DirtiesContext
    @Test
    void createNewBooking_itemNotAvailable_expectedError() {
        ItemDto newItemDto = itemService.createNewItem(new ItemInputDto("Отвертка", "Электрическая с батарейкой",
                false, null), owner.getId());
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusMinutes(2), newItemDto.getId());

        assertThrows(ValidatorException.class, () -> bookingService.createNewBooking(newBooking, booker.getId()));
    }

    @DirtiesContext
    @Test
    void createNewBooking_itemNotFound_expectedError() {
        BookingInputDto newBooking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().plusMinutes(2), 99L);

        assertThrows(NotFoundException.class, () -> bookingService.createNewBooking(newBooking, booker.getId()));
    }

    @DirtiesContext
    @Test
    void getBookingById_bookingExist_returnBookingDto() {
        BookingDto booking1 = bookingService.getBookingByIdAndUserId(booking.getId(), booker.getId());

        assertEquals(booking1.getId(), 1);
        assertEquals(booking1.getStatus(), Status.WAITING);
        assertEquals(booking1.getBooker().getId(), booker.getId());
        assertEquals(booking1.getBooker().getName(), booker.getName());
        assertEquals(booking1.getItem().getId(), item.getId());
        assertEquals(booking1.getItem().getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void getBookingById_bookingNotExist_notFound() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingByIdAndUserId(999L, 1L));
    }

    @DirtiesContext
    @Test
    void setStatusBookingReject_bookingExist_returnDto() {

        BookingDto booking1 = bookingService.setStatusBooking(booking.getId(), owner.getId(), false);

        assertEquals(booking1.getId(), 1);
        assertEquals(booking1.getStatus(), Status.REJECTED);
        assertEquals(booking1.getBooker().getId(), booker.getId());
        assertEquals(booking1.getBooker().getName(), booker.getName());
        assertEquals(booking1.getItem().getId(), item.getId());
        assertEquals(booking1.getItem().getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void setStatusBookingApproved_bookingExist_returnDto() {

        BookingDto booking1 = bookingService.setStatusBooking(booking.getId(), owner.getId(), true);

        assertEquals(booking1.getId(), 1);
        assertEquals(booking1.getStatus(), Status.APPROVED);
        assertEquals(booking1.getBooker().getId(), booker.getId());
        assertEquals(booking1.getBooker().getName(), booker.getName());
        assertEquals(booking1.getItem().getId(), item.getId());
        assertEquals(booking1.getItem().getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void setStatusBooking_tryNotOwnerSetStatus_expectedError() {
        assertThrows(NotFoundException.class, () -> bookingService.setStatusBooking(booking.getId(), booker.getId(),
                true));
        assertThrows(NotFoundException.class, () -> bookingService.setStatusBooking(owner.getId(), 999L,
                true));
    }

    @DirtiesContext
    @Test
    void setStatusBooking_setStatusDuplicated_expectedError() {
        bookingService.setStatusBooking(booking.getId(), owner.getId(), true);
        assertThrows(ValidatorException.class, () -> bookingService.setStatusBooking(booking.getId(), owner.getId(), true));
    }

    /**
     * Серия тесто для "Владельцев"
     */
    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_stateAll_returnListDto() {

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "ALL", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_statePast_returnListDto() throws Exception {

        TimeUnit.SECONDS.sleep(4);

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "PAST", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_stateWaiting_returnListDto() {

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "WAITING", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_stateReject_returnListDto() {

        BookingDto newBookingDto = bookingService.setStatusBooking(owner.getId(), booking.getId(), false);

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "REJECTED", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), newBookingDto.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), newBookingDto.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), newBookingDto.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), newBookingDto.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), newBookingDto.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), newBookingDto.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), newBookingDto.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_stateFuture_returnListDto() {
        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "FUTURE", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }


    @DirtiesContext
    @Test
    public void getAllBookingByStatusFromOwner_stateCurrent_returnListDto() throws Exception {
        TimeUnit.SECONDS.sleep(2);

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromOwner(owner.getId(), "CURRENT", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    public void getAllBookingByStatusFromOwner_userNotFound_expectedErorr() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingByStatusFromOwner(99L, "ALL", 0, 10));
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromOwner_stateUnsupported_returnException() {
        assertThrows(UnsupportedStatus.class, () -> bookingService.getAllBookingByStatusFromOwner(owner.getId(), "Unsupported", 0, 10));
    }

    /**
     * Серия тесто для "заёмщиков"
     */
    @DirtiesContext
    @Test
    void getAllBookingByStatusFromBooker_stateAll_returnListDto() {

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromBooker(booker.getId(), "ALL", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }


    @DirtiesContext
    @Test
    void getAllBookingByStatusFromFuture_stateAll_returnListDto() {

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromBooker(booker.getId(), "FUTURE", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromBooker_stateWaiting_returnListDto() {

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromBooker(booker.getId(), "WAITING", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromBooker_statePast_returnListDto() throws InterruptedException {

        TimeUnit.SECONDS.sleep(4);

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromBooker(booker.getId(), "PAST", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getAllBookingByStatusFromBooker_stateCurrent_returnListDto() throws InterruptedException {

        TimeUnit.SECONDS.sleep(2);

        List<BookingDto> bookingDtoList = bookingService.getAllBookingByStatusFromBooker(booker.getId(), "CURRENT", 0, 10);

        assertEquals(bookingDtoList.get(0).getId(), booking.getId());
        assertEquals(bookingDtoList.get(0).getStatus(), booking.getStatus());
        assertEquals(bookingDtoList.get(0).getEnd().getSecond(), booking.getEnd().getSecond());
        assertEquals(bookingDtoList.get(0).getStart().getSecond(), booking.getStart().getSecond());
        assertEquals(bookingDtoList.get(0).getItemId(), booking.getItemId());
        assertEquals(bookingDtoList.get(0).getItem().getId(), booking.getItem().getId());
        assertEquals(bookingDtoList.get(0).getBooker().getId(), booking.getBooker().getId());
    }

    @DirtiesContext
    @Test
    void getUsersBooking_stateUnsupport_returnException() {
        assertThrows(UnsupportedStatus.class, () -> bookingService.getAllBookingByStatusFromBooker(booker.getId(), "Ikibana", 0, 10));
    }

    @DirtiesContext
    @Test
    public void getUsersBooking_userNotFound_error() {
        assertThrows(NotFoundException.class, () -> bookingService.getAllBookingByStatusFromBooker(99L, "All", 0, 10));
    }


    @DirtiesContext
    @Test
    void getBookingByIdAndUserId_notExistBookingFromUser_returnExeption() {
        assertThrows(NotFoundException.class, () -> bookingService.getBookingByIdAndUserId(booking.getId(), 999L));
    }
}
