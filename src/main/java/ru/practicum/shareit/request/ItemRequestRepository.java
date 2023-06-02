package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findAllByRequesterIdOrderByCreatedDateDesc(Long userId);

    List<ItemRequest> findAllByRequesterIdNotOrderByCreatedDateDesc(Long userId, Pageable pageable);
}
