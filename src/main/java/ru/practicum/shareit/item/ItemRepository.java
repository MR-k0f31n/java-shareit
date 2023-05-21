package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> getItemsByOwnerId(Long ownerId);

    List<Item> findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAndAvailableTrue(String text, String text2);
}
