package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author MR.k0F31n
 */

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    private final Pageable pageable = PageRequest.of(0 / 10, 10);
    private ItemRequest request1;
    private ItemRequest request2;
    private User owner;
    private User otherUser;

    @DirtiesContext
    @BeforeEach
    void beforeEach() {
        userRepository.save(new User(null, "name", "1@email.com"));
        userRepository.save(new User(null, "name2", "2@email.com"));
        Optional<User> user1 = userRepository.findById(1L);
        Optional<User> user2 = userRepository.findById(2L);
        owner = user1.get();
        otherUser = user2.get();

        itemRequestRepository.save(new ItemRequest(null, "Need Door", owner, LocalDateTime.now()));
        itemRequestRepository.save(new ItemRequest(null, "Need Door", otherUser, LocalDateTime.now()));

        Optional<ItemRequest> requestId1 = itemRequestRepository.findById(1L);
        Optional<ItemRequest> requestId2 = itemRequestRepository.findById(2L);
        request1 = requestId1.get();
        request2 = requestId2.get();
    }

    @DirtiesContext
    @Test
    void findAllRequestFromOwner_returnListRequest_listSize1() {
        List<ItemRequest> list = itemRequestRepository.findAllByRequesterIdOrderByCreatedDateDesc(owner.getId(), pageable);

        assertEquals(1, list.size());
        assertTrue(list.contains(request1));
    }

    @DirtiesContext
    @Test
    void findAllOtherRequest_returnList_listSize1() {
        List<ItemRequest> list = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDateDesc(owner.getId(), pageable);

        assertEquals(1, list.size());
        assertTrue(list.contains(request2));
    }
}
