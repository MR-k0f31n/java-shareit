package ru.practicum.shareit.items;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author MR.k0F31n
 */
@JsonTest
public class CommentJsonTest {
    @Autowired
    private JacksonTester<CommentDto> jsonCommentDto;

    @Test
    void testCommentDto() throws Exception {
        LocalDateTime created = LocalDateTime.of(2025, 2, 20, 12, 10, 30);

        CommentDto commentDto = new CommentDto(1L, "Text from comment No1", 1L, "Name1",
                created);

        JsonContent<CommentDto> result = jsonCommentDto.write(commentDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Text from comment No1");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Name1");
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(created.toString());
    }
}
