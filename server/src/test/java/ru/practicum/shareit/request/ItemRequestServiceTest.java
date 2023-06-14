package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    final Pageable pageable = PageRequest.of(0 / 10, 10);
    private final UserService userService;
    private final ItemRequestService requestService;
    private final ItemService itemService;
    UserDto inputUser = new UserDto(null, "name", "email@mail.ru");
    ItemRequestInputDto inputRequest = new ItemRequestInputDto("I need items");

    @DirtiesContext
    @Test
    void createNewRequest_returnDto() {

        UserDto user = userService.createNewUser(inputUser);

        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());

        assertEquals(request.getId(), 1);
        assertEquals(request.getDescription(), "I need items");
        assertEquals(request.getRequesterId(), user.getId());
    }

    @DirtiesContext
    @Test
    void getAllItemRequestFromOtherUsers_returnRequestWitchAnswer() {
        UserDto user = userService.createNewUser(inputUser);
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());

        ItemDto item = itemService.createNewItem(new ItemInputDto("Its need", "100%", true,
                request.getId()), user.getId());

        ItemRequestWithAnswerDto requests = requestService.getAllItemRequestFromOtherUsers(request.getId(), user.getId());

        assertEquals(requests.getItems().get(0).getId(), item.getId());
        assertEquals(requests.getItems().get(0).getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void getAllItemRequestFromOtherUsers_requestNotExist() {
        UserDto user = userService.createNewUser(inputUser);
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());

        assertThrows(NotFoundException.class, () -> requestService
                .getAllItemRequestFromOtherUsers(999L, user.getId()));
    }

    @DirtiesContext
    @Test
    void getAllItemRequestByUserIdWithAnswer_returnRequestWitchAnswer() {
        UserDto user = userService.createNewUser(inputUser);
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());
        ItemDto item = itemService.createNewItem(new ItemInputDto("Its need", "100%", true,
                request.getId()), user.getId());

        List<ItemRequestWithAnswerDto> requests = requestService.getAllItemRequestByUserIdWithAnswer(user.getId(), pageable);

        assertEquals(requests.get(0).getItems().get(0).getId(), item.getId());
        assertEquals(requests.get(0).getItems().get(0).getName(), item.getName());
    }

    @DirtiesContext
    @Test
    void getAllRequest_returnRequestWitchAnswer() {
        UserDto user = userService.createNewUser(inputUser);
        UserDto user2 = userService.createNewUser(new UserDto(null, "name2", "em@mai.ru"));
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());
        ItemDto item = itemService.createNewItem(new ItemInputDto("Its need", "100%", true,
                request.getId()), user.getId());

        List<ItemRequestWithAnswerDto> requests = requestService.getAllRequest(user2.getId(), pageable);

        assertEquals(requests.get(0).getItems().get(0).getId(), item.getId());
        assertEquals(requests.get(0).getItems().get(0).getName(), item.getName());
    }
}
