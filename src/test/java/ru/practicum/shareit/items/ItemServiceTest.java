package ru.practicum.shareit.items;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingInputDto;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.request.ItemRequestDto;
import ru.practicum.shareit.request.ItemRequestInputDto;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private final UserService service;
    private final ItemService items;
    private final ItemRequestService itemRequestService;
    private final BookingService bookingService;

    private final UserDto user1 = new UserDto(1L, "name1", "emai1@mail.com");
    private final ItemInputDto inputItem = new ItemInputDto("Отвертка", "Отвертка в печень ни один тест не вечен",
            true, null);
    private final BookingInputDto inputDto = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
            LocalDateTime.now().plusMinutes(2), 1L);


    @DirtiesContext
    @Test
    void createItemFromRequest_returnItemWitchRequest() {
        UserDto user = service.createNewUser(new UserDto(null, "name", "mail@mail.ru"));
        ItemRequestDto request = itemRequestService.createNewItemRequest(new ItemRequestInputDto("need item"),
                user.getId());
        ItemDto item = items.createNewItem(new ItemInputDto("Item", "descr", true, request.getId()),
                user.getId());

        assertEquals(user.getId(), item.getRequestId());
    }

    @DirtiesContext
    @Test
    void createItemFromRequest_requestNotFound() {
        UserDto user = service.createNewUser(new UserDto(null, "name", "mail@mail.ru"));
        ItemRequestDto request = itemRequestService.createNewItemRequest(new ItemRequestInputDto("need item"),
                user.getId());
        assertThrows(NotFoundException.class, () -> items.createNewItem(new ItemInputDto("Item", "descr", true, 999L),
                user.getId()));
    }

    @DirtiesContext
    @Test
    void updateUser_inputNull_returnNull() {
        UserDto updateUser = user1;
        updateUser.setId(1L);
        updateUser.setName(null);
        updateUser.setEmail(null);

        assertNull(service.updateUser(updateUser, 1L), "Обновились в null!");
    }

    @DirtiesContext
    @Test
    void updateItem_AllUpdate_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setName("ОтВерТка апргрейд");

        ItemDto itemBeforeUpdateName = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateName.getName());

        item1.setDescription("Убивает на растоянии");

        ItemDto itemBeforeUpdateDesc = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateDesc.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateDesc.getDescription());

        item1.setAvailable(false);

        ItemDto itemBeforeUpdateAvl = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateAvl.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateAvl.getDescription());
        assertEquals(item1.getAvailable(), itemBeforeUpdateAvl.getAvailable());
    }

    @DirtiesContext
    @Test
    void updateItem_userNotOwner_expectedError() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setName("ОтВерТка апргрейд");

        assertThrows(NotFoundException.class, () -> items.updateItem(item1, item1.getId(), 2L));
    }

    @DirtiesContext
    @Test
    void getItem_itemNotFound_userExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 1L));
    }

    @DirtiesContext
    @Test
    void getItem_itemNotFound_userNotExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 2L));
    }

    @DirtiesContext
    @Test
    void getItem_returnDto() {
        final UserDto userOne = new UserDto(1L, "name1", "emai2313@mail.com");
        UserDto userDto = service.createNewUser(userOne);
        ItemDto item1 = items.createNewItem(inputItem, userDto.getId());
        ItemDto findItem = items.getItemDtoById(item1.getId(), userDto.getId());
        assertEquals(item1.getName(), findItem.getName());
        assertEquals(item1.getDescription(), findItem.getDescription());
        items.deleteItem(item1.getId(), userDto.getId());
    }

    @DirtiesContext
    @Test
    void getItemNotOwner_returnDto() {
        UserDto user = service.createNewUser(user1);
        UserDto user2 = new UserDto(2L, "name2", "emai2@mail.com");
        service.createNewUser(user2);

        ItemDto item1 = items.createNewItem(inputItem, user.getId());
        ItemDto findItem = items.getItemDtoById(item1.getId(), user2.getId());

        assertEquals(item1.getName(), findItem.getName());
        assertEquals(item1.getDescription(), findItem.getDescription());

        items.deleteItem(item1.getId(), user.getId());
        service.deleteUserById(user2.getId());
    }

    @DirtiesContext
    @Test
    void searchItem_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        List<ItemDto> findItem = items.searchItem("ОтВЕРТка", 0, 10);
        assertEquals(item1.getName(), findItem.get(0).getName());
        assertEquals(item1.getDescription(), findItem.get(0).getDescription());
        items.deleteItem(item1.getId(), user1.getId());
    }

    @DirtiesContext
    @Test
    void searchItem_emptyRequest_emptyList() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        List<ItemDto> findItem = items.searchItem("", 0, 10);
        assertEquals(0, findItem.size());
        items.deleteItem(item1.getId(), user1.getId());
    }

    @DirtiesContext
    @Test
    void searchItem_emptySpace_emptyList() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        List<ItemDto> findItem = items.searchItem("  ", 0, 10);
        assertEquals(0, findItem.size());
        items.deleteItem(item1.getId(), user1.getId());
    }

    @DirtiesContext
    @Test
    void addComment_textIsEmpty() {
        CommentInputDto input = new CommentInputDto();
        input.setText(" ");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }

    @DirtiesContext
    @Test
    void addComment_userNotRentItem() {
        CommentInputDto input = new CommentInputDto();
        input.setText("User not rent Item");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }

    @DirtiesContext
    @Test
    void deleteItemItem() {
        UserDto userDto = service.createNewUser(user1);
        ItemDto itemFromDelete = items.createNewItem(new ItemInputDto("ItemFromDelete", "from delete desc", true, null), userDto.getId());
        items.deleteItem(itemFromDelete.getId(), 1L);
    }

    @DirtiesContext
    @Test
    void deleteItemItem_userNotFound_expectedError() {
        assertThrows(NotFoundException.class, () -> items.deleteItem(1L, 999L));
    }

    @DirtiesContext
    @Test
    void deleteItemItem_itemNotFound_expectedError() {
        assertThrows(NotFoundException.class, () -> items.deleteItem(999L, 1L));
    }

    @DirtiesContext
    @Test
    void deleteItemItem_userNotOwner_expectedError() {
        UserDto userDto = service.createNewUser(user1);
        UserDto userDto1 = service.createNewUser(new UserDto(2L, "kto-to", "ctoTo@mail.com"));
        ItemDto itemFromDelete = items.createNewItem(new ItemInputDto("ItemFromDelete", "from delete desc", true, null), userDto.getId());
        assertThrows(NotFoundException.class, () -> items.deleteItem(itemFromDelete.getId(), userDto1.getId()));
    }
}
