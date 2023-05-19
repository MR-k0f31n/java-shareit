package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingDto createNewBooking(@RequestBody @Valid BookingInputDTO bookingInputDTODto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.debug("Endpoint request: 'POST /bookings'");
        return bookingService.createNewBooking(bookingInputDTODto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto setStatusBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @PathVariable Long bookingId,
                                       @RequestParam(name = "approved") Boolean isApproved) {
        return bookingService.setStatusBooking(bookingId, userId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        return bookingService.getBookingByIdAndUserId(bookingId, userId);
    }

    @GetMapping
    public List<BookingDto> getByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByStatusFromBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getByOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long ownerId,
            @RequestParam(name = "state", defaultValue = "ALL") String state) {
        return bookingService.getAllBookingByStatusFromOwner(ownerId, state);
    }
}
