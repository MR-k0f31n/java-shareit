package ru.practicum.shareit.comment;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author MR.k0F31n
 */
@Getter
public class CommentInputDto {
    @NotNull(message = "Comments is null!")
    @NotBlank(message = "Comments is empty!")
    private String text;
}
