package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingInputDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceAndItemServiceWitchContextTest {

    final UserDto user1 = new UserDto(1L, "name1", "emai1@mail.com");
    final ItemInputDto inputItem = new ItemInputDto("Отвертка", "Отвертка в печень ни один тест не вечен",
            true, null);
    private final UserService service;
    private final ItemService items;
    private final BookingInputDto inputDto = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
            LocalDateTime.now().plusMinutes(2), 1L);

    @Test
    void updateUserEmailInContext_expectedCorrect_returnUserDtoBeforeUpdate() {
        service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setEmail("emailUpdate@mail.com");

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertEquals(updateUser.getEmail(), userBeforeUpdate.getEmail(), "Email not update");
    }

    @Test
    void updateUserNameInContext_expectedCorrect_returnUserDtoBeforeUpdate() {
        service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setName("Update name");

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertEquals(updateUser.getName(), userBeforeUpdate.getName(), "Name not update");
    }

    @Test
    void deleteUser_correct() {
        service.createNewUser(user1);
        ItemInputDto item1 = new ItemInputDto("name1", "descr1", true, null);
        ItemInputDto item2 = new ItemInputDto("name1", "descr1", true, null);
        items.createNewItem(item1, user1.getId());
        items.createNewItem(item2, user1.getId());
        assertEquals(1, service.findAllUser().size(), "Юзеров нет");
        assertEquals(4, items.getAllItemsByOwner(user1.getId(), 0, 10).size(), "Итемов нет");

        service.deleteUserById(1L);

        assertEquals(0, service.findAllUser().size(), "Юзер не удалился");
        assertThrows(NotFoundException.class, () -> items.getAllItemsByOwner(user1.getId(), 0, 10),
                "User not deleted");
    }

    @Test
    void updateUser_inputNull_returnNull() {
        service.createNewUser(user1);
        UserDto updateUser = user1;
        updateUser.setName(null);
        updateUser.setEmail(null);

        assertNull(service.updateUser(updateUser, 1L), "Обновились в null!");
    }

    @Test
    void updateItem_AllUpdate_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setId(1L);
        item1.setName("ОтВерТка апргрейд");

        ItemDto itemBeforeUpdateName = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateName.getName());

        item1.setDescription("Убивает урков на растоянии");

        ItemDto itemBeforeUpdateDesc = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateDesc.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateDesc.getDescription());

        item1.setAvailable(false);

        ItemDto itemBeforeUpdateAvl = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateAvl.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateAvl.getDescription());
        assertEquals(item1.getAvailable(), itemBeforeUpdateAvl.getAvailable());
    }

    @Test
    void updateItem_userNotOwner_expectedError() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setId(1L);
        item1.setName("ОтВерТка апргрейд");

        assertThrows(NotFoundException.class, () -> items.updateItem(item1, item1.getId(), 2L));
    }

    @Test
    void getItem_itemNotFound_userExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 1L));
    }

    @Test
    void getItem_itemNotFound_userNotExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 2L));
    }

    @Test
    void addComment_textIsEmpty() {
        CommentInputDto input = new CommentInputDto();
        input.setText(" ");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }

    @Test
    void addComment_userNotRentItem() {
        CommentInputDto input = new CommentInputDto();
        input.setText("User not rent Item");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }
}
