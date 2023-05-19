package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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
    private Item item;
    @NotNull
    private User author;
}
