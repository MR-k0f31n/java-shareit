package ru.practicum.shareit.booking;

import lombok.Getter;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author MR.k0F31n
 */
@Getter
public class BookingInputDTO {
    @FutureOrPresent(message = "Rental start time cannot be later now time")
    @NotNull(message = "Start time is empty")
    private LocalDateTime start;
    @Future(message = "Rental end time of the lease must be in future time")
    @NotNull(message = "End time is empty")
    private LocalDateTime end;
    @NotNull(message = "ID item empty")
    private Long itemId;
}
