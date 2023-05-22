package ru.practicum.shareit.comment;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class CommentMapper {
    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                commentDto.getId(),
                commentDto.getText(),
                new Item(),
                new User(),
                commentDto.getCreated()
        );
    }

    public static Comment toComment(CommentInputDto commentInputDto) {
        return new Comment(
                null,
                commentInputDto.getText(),
                new Item(),
                new User(),
                LocalDateTime.now()
        );
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getComment(),
                comment.getItem().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static List<CommentDto> toCommentDtoList(Iterable<Comment> iterable) {
        List<CommentDto> result = new ArrayList<>();
        for (Comment comment : iterable) {
            result.add(toCommentDto(comment));
        }
        return result;
    }
}
