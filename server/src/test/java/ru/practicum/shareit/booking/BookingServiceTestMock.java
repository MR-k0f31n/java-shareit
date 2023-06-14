package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatus;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


/**
 * @author MR.k0F31n
 */

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTestMock {
    @MockBean
    private final BookingService service;
    @MockBean
    private final BookingRepository repository;


    private final BookingInputDto inputDto = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
            LocalDateTime.now().plusMinutes(2), 1L);
    private final BookingDto bookingDto = new BookingDto(1L, inputDto.getStart(), inputDto.getEnd(), inputDto.getItemId(),
            new Item(), new User(), Status.WAITING);
    private final Booking booking = new Booking(1L, inputDto.getStart(), inputDto.getEnd(), new Item(), new User(),
            Status.WAITING);
    private final Pageable pageable = PageRequest.of(0 / 10, 10);

    @Test
    void createNewBooking_expectedCorrect_returnDto() {
        when(service.createNewBooking(any(BookingInputDto.class), anyLong())).thenReturn(bookingDto);

        BookingDto booking = service.createNewBooking(inputDto, 1L);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItemId(), booking.getItemId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void createNewBooking_bookingTimeRentIsNotCorrect_expectedError() {
        when(service.createNewBooking(any(BookingInputDto.class), anyLong())).thenThrow(ValidatorException.class);

        final BookingInputDto booking = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
                LocalDateTime.now().minusMinutes(2), 1L);

        assertThrows(ValidatorException.class, () -> service.createNewBooking(booking, 1L));
    }

    @Test
    void createNewBooking_bookingCreatedOwnerItems_expectedError() {
        when(service.createNewBooking(any(BookingInputDto.class), anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.createNewBooking(inputDto, 2L));
    }

    @Test
    void setStatusBooking_returnBookingDto() {
        when(service.setStatusBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        BookingDto booking = service.setStatusBooking(1L, 1L, true);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItemId(), booking.getItemId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void createNewBooking_bookingStatusNotWaiting_expectedError() {
        when(service.setStatusBooking(anyLong(), anyLong(), anyBoolean())).thenThrow(ValidatorException.class);

        assertThrows(ValidatorException.class, () -> service.setStatusBooking(1L, 1L, true));
    }

    @Test
    void createNewBooking_ownerApproveBooking_expectedError() {
        when(service.setStatusBooking(anyLong(), anyLong(), anyBoolean())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.setStatusBooking(1L, 1L, true));
    }

    @Test
    void getBookingById_returnDto() {
        when(service.getBookingByIdAndUserId(anyLong(), anyLong())).thenReturn(bookingDto);

        BookingDto booking = service.getBookingByIdAndUserId(1L, 1L);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItemId(), booking.getItemId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }

    @Test
    void getAllBookingByStatusFromOwner_stateAll_returnListDto() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromOwner(1L, "All", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromOwner_statePAST_returnListDto() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromOwner(1L, "PAST", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromOwner_stateFUTURE_returnListDto() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromOwner(1L, "FUTURE", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromOwner_stateWAITING_returnListDto() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromOwner(1L, "WAITING", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromOwner_stateREJECTED_returnListDto() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromOwner(1L, "REJECTED", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromOwner_stateUnsupportedStatus_exeption() {
        when(service.getAllBookingByStatusFromOwner(anyLong(), anyString(), any(Pageable.class)))
                .thenThrow(UnsupportedStatus.class);

        assertThrows(UnsupportedStatus.class, () -> service.getAllBookingByStatusFromOwner(1L,
                "AVADAKEDABRA_TESTING!", pageable));
    }

    @Test
    void getAllBookingByStatusFromBooker_stateAll_returnListDto() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromBooker(1L, "All", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromBooker_statePAST_returnListDto() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromBooker(1L, "PAST", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromBooker_stateFUTURE_returnListDto() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromBooker(1L, "FUTURE", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromBooker_stateWAITING_returnListDto() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromBooker(1L, "WAITING", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromBooker_stateREJECTED_returnListDto() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(List.of(bookingDto));

        List<BookingDto> actualBookings = service.getAllBookingByStatusFromBooker(1L, "REJECTED", pageable);

        assertTrue(actualBookings.contains(bookingDto));
    }

    @Test
    void getAllBookingByStatusFromBooker_stateUnsupportedStatus_exeption() {
        when(service.getAllBookingByStatusFromBooker(anyLong(), anyString(), any(Pageable.class)))
                .thenThrow(UnsupportedStatus.class);

        assertThrows(UnsupportedStatus.class, () -> service.getAllBookingByStatusFromBooker(1L,
                "AVADAKEDABRA_TESTING!", pageable));
    }

    @Test
    void getBookingByIdAndUserId_returnDto() {
        when(service.getBookingByIdAndUserId(anyLong(), anyLong())).thenReturn(bookingDto);

        BookingDto booking = service.getBookingByIdAndUserId(1L, 1L);

        assertNotNull(booking);
        assertEquals(bookingDto.getId(), booking.getId());
        assertEquals(bookingDto.getItemId(), booking.getItemId());
        assertEquals(bookingDto.getStatus(), booking.getStatus());
    }
}
