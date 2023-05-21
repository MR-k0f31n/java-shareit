package ru.practicum.shareit.booking;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author MR.k0F31n
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingItemBookerDto {
    private Long id;
    private Long itemId;
    private Long bookerId;
    private LocalDateTime start;
    private LocalDateTime end;
}
