package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private String authorName;
    private LocalDateTime created;
}
