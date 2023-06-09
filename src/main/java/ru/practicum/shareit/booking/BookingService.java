package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface BookingService {
    BookingDto createNewBooking(BookingInputDto bookingInputDTODto, Long userId);

    BookingDto setStatusBooking(Long bookingId, Long userId, Boolean isApproved);

    List<BookingDto> getAllBookingByStatusFromOwner(Long userId, String status, Pageable pageable);

    List<BookingDto> getAllBookingByStatusFromBooker(Long userId, String status, Pageable pageable);

    BookingDto getBookingByIdAndUserId(Long bookingId, Long userId);
}
