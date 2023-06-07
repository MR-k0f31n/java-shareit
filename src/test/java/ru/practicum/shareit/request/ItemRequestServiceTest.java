package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
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
    void createNewRequest_createAnswer_returnDtoWitchAnswer() {
        UserDto user = userService.createNewUser(inputUser);
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());
        ItemDto item = itemService.createNewItem(new ItemInputDto("Its need", "100%", true,
                request.getId()), user.getId());

        ItemRequestWithAnswerDto request1 = requestService.getAllItemRequestFromOtherUsers(user.getId(), request.getId());

        assertEquals(request1.getItems().get(0).getId(), item.getId());
        assertEquals(request1.getItems().get(0).getName(), item.getName());
    }


    @DirtiesContext
    @Test
    void getAllItemRequestByUserIdWithAnswer_returnRequestWitchAnswer() {
        UserDto user = userService.createNewUser(inputUser);
        ItemRequestDto request = requestService.createNewItemRequest(inputRequest, user.getId());
        ItemDto item = itemService.createNewItem(new ItemInputDto("Its need", "100%", true,
                request.getId()), user.getId());

        List<ItemRequestWithAnswerDto> requests = requestService.getAllItemRequestByUserIdWithAnswer(user.getId(), 0, 10);

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

        List<ItemRequestWithAnswerDto> requests = requestService.getAllRequest(user2.getId(), 0, 10);

        assertEquals(requests.get(0).getItems().get(0).getId(), item.getId());
        assertEquals(requests.get(0).getItems().get(0).getName(), item.getName());
    }
}
