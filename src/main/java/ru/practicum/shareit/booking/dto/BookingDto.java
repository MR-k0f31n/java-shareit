package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.Status;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime startRent;
    private LocalDateTime endRent;
    private Long itemId;
    private Long bookerId;
    private Enum<Status> status;
}
