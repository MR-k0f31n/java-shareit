package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

/**
 * @author MR.k0F31n
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
    Collection<Item> getItemsByOwnerId(Long ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', ?1, '%')) " +
            "OR LOWER(i.description) LIKE LOWER(CONCAT('%', ?1, '%')) AND i.available = true")
    Collection<Item> searchItems(String searchByName);
}
