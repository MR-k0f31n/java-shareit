package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author MR.k0F31n
 */

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final Pageable pageable = PageRequest.of(0 / 10, 10);
    private User owner;
    private User booker;
    private Item item;
    private Booking booking;

    @DirtiesContext
    @BeforeEach
    void beforeEach() {
        userRepository.save(new User(null, "name1", "email1@mail.com"));
        userRepository.save(new User(null, "name2", "email2@mail.com"));
        Optional<User> user1 = userRepository.findById(1L);
        Optional<User> user2 = userRepository.findById(2L);

        owner = user1.get();
        booker = user2.get();

        itemRepository.save(new Item(null, "Дверь", "Что бы выйти", true, owner, null));
        Optional<Item> itemId1 = itemRepository.findById(1L);
        item = itemId1.get();

        bookingRepository.save(new Booking(null, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(3),
                item, booker, Status.WAITING));
        Optional<Booking> bookingId1 = bookingRepository.findById(1L);
        booking = bookingId1.get();
    }

    @DirtiesContext
    @Test
    void findByBooker_returnList_listSize1() {
        List<Booking> list = bookingRepository.findAllByBookerIdOrderByStartRentDesc(booker.getId(), pageable);

        assertEquals(1, list.size());
        assertTrue(list.contains(booking));
    }

    @DirtiesContext
    @Test
    void testCheckRentedItemByUse_dateBeforeEndRentDate_returnBoolean() throws Exception {

        TimeUnit.SECONDS.sleep(4);

        assertTrue(bookingRepository.existsByBookerIdAndItemIdAndEndRentBefore(booker.getId(), item.getId(), LocalDateTime.now()));
    }
}
