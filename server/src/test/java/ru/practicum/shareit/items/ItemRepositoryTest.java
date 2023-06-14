package ru.practicum.shareit.items;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author MR.k0F31n
 */
@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final Pageable pageable = PageRequest.of(0 / 10, 10);
    private User owner;
    private Item item1;
    private Item item2;
    private ItemRequest itemRequest;

    @DirtiesContext
    @BeforeEach
    void beforeEach() {
        userRepository.save(new User(null, "name", "1@email.com"));
        Optional<User> user = userRepository.findById(1L);

        owner = user.get();
        itemRequestRepository.save(new ItemRequest(null, "Need Door", owner, LocalDateTime.now()));
        Optional<ItemRequest> itemRequestId1 = itemRequestRepository.findById(1L);
        itemRequest = itemRequestId1.get();

        itemRepository.save(new Item(null, "Дверь", "Что бы выйти", true, owner, null));
        itemRepository.save(new Item(null, "Ручка", "Что бы открыть дверь", true, owner, itemRequest));
        Optional<Item> itemId1 = itemRepository.findById(1L);
        Optional<Item> itemId2 = itemRepository.findById(2L);

        item1 = itemId1.get();
        item2 = itemId2.get();
    }

    @DirtiesContext
    @Test
    void getItemByRequestId_returnList_listContainsItem2() {
        List<Item> itemList = itemRepository.findAllByRequestId(1L);

        assertTrue(itemList.contains(item2));
    }

    @DirtiesContext
    @Test
    void getItemByOwner_returnListLength2_listContainsItem1AndItem2() {
        List<Item> itemList = itemRepository.findAllByOwnerId(1L, pageable);

        assertTrue(itemList.contains(item1));
        assertTrue(itemList.contains(item2));
    }

    @DirtiesContext
    @Test
    void searchTest_ignoreCase_returnListLength2() {
        String textSearch = "ДВЕРЬ";
        List<Item> itemList = itemRepository
                .findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAndAvailableTrue(textSearch,
                        textSearch, pageable);

        assertTrue(itemList.contains(item1));
        assertTrue(itemList.contains(item2));
    }
}
