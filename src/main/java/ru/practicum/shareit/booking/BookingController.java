package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createNewBooking(@RequestBody @Valid BookingInputDto booking, @RequestHeader("X-Sharer-User-Id") Long userId) {
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
    public List<BookingDto> getBookingsByBookerId(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                  @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                  @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.debug("Endpoint request: 'GET /bookings/state='" + state + "''");
        return bookingService.getAllBookingByStatusFromBooker(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwnerId(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(defaultValue = "10") @Min(1) Integer size,
                                                 @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                 @RequestParam(name = "state", defaultValue = "ALL") String state) {
        log.debug("Endpoint request: 'GET /bookings/owner/state='" + state + "''");
        return bookingService.getAllBookingByStatusFromOwner(ownerId, state, from, size);
    }
}
