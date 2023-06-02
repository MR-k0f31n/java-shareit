package ru.practicum.shareit.booking;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface BookingService {
    BookingDto createNewBooking(BookingInputDto bookingInputDTODto, Long userId);

    BookingDto setStatusBooking(Long bookingId, Long userId, Boolean isApproved);

    List<BookingDto> getAllBookingByStatusFromOwner(Long userId, String status, Integer from, Integer size);

    List<BookingDto> getAllBookingByStatusFromBooker(Long userId, String status, Integer from, Integer size);

    BookingDto getBookingByIdAndUserId(Long bookingId, Long userId);
}
