package ru.practicum.shareit.item.comment;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author MR.k0F31n
 */
@Data
public class CommentInputDto {
    @NotNull(message = "Comments is null!")
    @NotBlank(message = "Comments is empty!")
    private String text;
}
