package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.BookingShortDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.User;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private BookingShortDto nextBooking;
    private BookingShortDto lastBooking;
    private List<CommentDto> comments;
    private Long requestId;
}
