package ru.practicum.shareit.items;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentRepositoryTest {
    private final CommentRepository commentRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @DirtiesContext
    @Test
    void findAllCommentByItemId() {
        userRepository.save(new User(null, "name", "1@email.com"));
        Optional<User> user = userRepository.findById(1L);

        itemRepository.save(new Item(null, "Дверь", "Что бы выйти", true, user.get(), null));
        itemRepository.save(new Item(null, "Ручка", "Что бы открыть дверь", true, user.get(), null));
        Optional<Item> itemId1 = itemRepository.findById(1L);
        Optional<Item> itemId2 = itemRepository.findById(2L);

        commentRepository.save(new Comment(null, "This text from comment1 user1 item1", itemId1.get(), user.get(),
                LocalDateTime.now()));
        commentRepository.save(new Comment(null, "This text from comment2 user1 item1", itemId1.get(), user.get(),
                LocalDateTime.now()));
        commentRepository.save(new Comment(null, "This text from comment3 user1 item1", itemId1.get(), user.get(),
                LocalDateTime.now()));

        commentRepository.save(new Comment(null, "This text from comment1 user1 item2", itemId2.get(), user.get(),
                LocalDateTime.now()));

        List<Comment> item1Comment = commentRepository.findAllByItemId(1L);
        List<Comment> item2Comment = commentRepository.findAllByItemId(2L);

        assertEquals(3, item1Comment.size());
        assertEquals(1, item2Comment.size());
        assertEquals("This text from comment1 user1 item2", item2Comment.get(0).getComment());
    }
}
