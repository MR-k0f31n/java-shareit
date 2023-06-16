package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createNewBooking(@RequestBody BookingInputDto booking, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Endpoint request: 'POST /bookings'");
        return bookingService.createNewBooking(booking, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatusBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam(name = "approved") Boolean isApproved) {
        log.debug("Endpoint request: 'PATCH /bookings/{'" + bookingId + "'}/approved='" + isApproved + "' '");
        return bookingService.setStatusBooking(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        log.debug("Endpoint request: 'GET /bookings/{'" + bookingId + "'}'");
        return bookingService.getBookingByIdAndUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBookerId(@RequestParam Integer from,
                                                  @RequestParam Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                  @RequestParam String state) {
        log.debug("Endpoint request: 'GET /bookings/state='" + state + "''");
        final Pageable pageable = PageRequest.of(from / size, size);
        return bookingService.getAllBookingByStatusFromBooker(bookerId, state, pageable);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestParam Integer from,
                                                 @RequestParam Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                 @RequestParam String state) {
        log.debug("Endpoint request: 'GET /bookings/owner/state='" + state + "''");
        final Pageable pageable = PageRequest.of(from / size, size);
        return bookingService.getAllBookingByStatusFromOwner(ownerId, state, pageable);
    }
}
