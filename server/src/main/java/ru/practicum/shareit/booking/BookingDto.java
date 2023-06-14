package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class BookingDto {
    private Long id;
    @FutureOrPresent(message = "Rental start time cannot be later now time")
    @NotNull(message = "Start time is empty")
    private LocalDateTime start;
    @Future(message = "Rental end time of the lease must be in future time")
    @NotNull(message = "End time is empty")
    private LocalDateTime end;
    @NotNull(message = "ID item empty")
    private Long itemId;
    private Item item;
    private User booker;
    private Status status;
}
