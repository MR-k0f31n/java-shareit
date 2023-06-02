package ru.practicum.shareit.item;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOwnerId(Long ownerId, Pageable pageable);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAndAvailableTrue(String text,
                                                                                                     String text2,
                                                                                                     Pageable pageable);

    List<Item> findAllByRequestId(Long requestId);
}
