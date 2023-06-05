package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceWitchContextTest {
    final UserDto user1 = new UserDto(1L, "name1", "emai1@mail.com");
    private final UserService service;
    private final ItemService items;

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
        assertEquals(2, items.getAllItemsByOwner(user1.getId(), 0, 10).size(), "Итемов нет");

        service.deleteUserById(1L);

        assertEquals(0, service.findAllUser().size(), "Юзер не удалился");
        assertThrows(NotFoundException.class, () -> items.getAllItemsByOwner(user1.getId(), 0, 10)
                , "User not deleted");
    }

    @Test
    void updateUser_inputNull_returnNull() {
        service.createNewUser(user1);
        UserDto updateUser = user1;
        updateUser.setName(null);
        updateUser.setEmail(null);

        assertNull(service.updateUser(updateUser, 1L), "Обновились в null!");
    }
}
