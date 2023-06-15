package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author MR.k0F31n
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookingInputDto {
    private LocalDateTime start;
    private LocalDateTime end;
    private Long itemId;
}
