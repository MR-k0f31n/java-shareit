package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingInputDto;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    final UserDto user1 = new UserDto(1L, "name1", "emai1@mail.com");
    final ItemInputDto inputItem = new ItemInputDto("Отвертка", "Отвертка в печень ни один тест не вечен",
            true, null);
    private final UserService service;
    private final ItemService items;
    private final BookingInputDto inputDto = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
            LocalDateTime.now().plusMinutes(2), 1L);
    private final Pageable pageable = PageRequest.of(0 / 10, 10);

    @Test
    void updateUserEmailInContext_expectedCorrect_returnUserDtoBeforeUpdate() {
        service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setEmail("emailUpdate@mail.com");

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertEquals(updateUser.getEmail(), userBeforeUpdate.getEmail(), "Email not update");
    }

    @Test
    void updateUser_nameIsBlank_notUpdate() {
        UserDto user = service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setName("");

        UserDto userBeforeUpdate = service.updateUser(updateUser, user.getId());

        assertEquals("name1", userBeforeUpdate.getName());
    }

    @DirtiesContext
    @Test
    void updateUser_nameIsNull_notUpdate() {
        UserDto userOne = new UserDto(1L, "name1", "emai11120@mail.com");
        UserDto user = service.createNewUser(userOne);

        UserDto updateUser = userOne;
        updateUser.setName(null);

        UserDto userBeforeUpdate = service.updateUser(updateUser, user.getId());

        assertEquals("name1", userBeforeUpdate.getName());
    }

    @Test
    void updateUser_emailIsBlank_notUpdate() {
        UserDto user = service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setEmail("");

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertEquals(user.getEmail(), userBeforeUpdate.getEmail());
    }

    @Test
    void updateUser_emailIsNull_notUpdate() {
        UserDto user = service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setEmail(null);

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertEquals(user.getEmail(), userBeforeUpdate.getEmail());
    }

    @DirtiesContext
    @Test
    void updateUser_emailIsExist_expectedError() {
        UserDto userOne = service.createNewUser(user1);
        UserDto userDto = new UserDto(2L, "name2", "emai@mail.cin");
        UserDto userTwo = service.createNewUser(userDto);
        userTwo.setEmail(userOne.getEmail());

        assertThrows(Exception.class, () -> service.updateUser(userTwo, userTwo.getId()));
    }

    @Test
    void updateUser_allNull_returnNull() {
        UserDto user = service.createNewUser(user1);

        UserDto updateUser = user1;
        updateUser.setEmail(null);
        updateUser.setName(null);

        UserDto userBeforeUpdate = service.updateUser(updateUser, updateUser.getId());

        assertNull(userBeforeUpdate);
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
        assertEquals(2, items.getAllItemsByOwner(user1.getId(), pageable).size(), "Итемов нет");

        service.deleteUserById(1L);

        assertEquals(0, service.findAllUser().size(), "Юзер не удалился");
        assertThrows(NotFoundException.class, () -> items.getAllItemsByOwner(user1.getId(), pageable),
                "User not deleted");
    }
}
