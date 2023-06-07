package ru.practicum.shareit.items;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentMapperTest {
    @Test
    void toComment() {
        CommentInputDto input = new CommentInputDto();
        input.setText("text");

        Comment comment = CommentMapper.toComment(input);

        assertEquals(input.getText(), comment.getComment());
    }

    @Test
    void toCommentDto() {
        Comment comment = new Comment(1L, "text", new Item(), new User(), LocalDateTime.now());

        CommentDto commentDto = CommentMapper.toCommentDto(comment);

        assertEquals(comment.getComment(), commentDto.getText());
    }
}
