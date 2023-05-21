package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author MR.k0F31n
 */
@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;
    @NotBlank
    @NotNull
    private String text;
    @NotNull
    private Long itemId;
    @NotNull
    private User author;
    private LocalDateTime created;
}
