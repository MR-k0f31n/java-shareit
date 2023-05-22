package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByItemId(Long itemId);

    Comment findByAuthorIdAndItemId(Long userId, Long itemId);

    Boolean existsByAuthorIdAndItemId(Long userId, Long itemId);
}
