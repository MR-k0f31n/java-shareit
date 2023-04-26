package ru.practicum.shareit.testInMemoryFunction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
public class UserServiceTest {
    private final UserService service;

    @Autowired
    public UserServiceTest(UserService service) {
        this.service = service;
    }

    @Test
    void createTest_AssertTrue() {
        User user = new User(null, "Name", "email@email.com");
        UserDto userDto = service.createNewUser(user);

        User user2 = new User(null, "Name2", "email2@email.com");
        UserDto userDto2 = service.createNewUser(user2);

        assertEquals(1L, userDto.getId());
        assertEquals("Name", userDto.getName());
        assertEquals("email@email.com", userDto.getEmail());
    }

    @Test
    void createTest_ExpectedThrows() {
        User userEmailDuplicate = new User(null, "Name Duplicate", "email@email.com");
        Throwable throwable = assertThrows(EmailConflictException.class,
                () -> service.createNewUser(userEmailDuplicate));
        assertEquals("This email '" + userEmailDuplicate.getEmail() + "' not unique, please check email",
                throwable.getMessage(), "Тест на проверку дубликата емейл провален!");
    }

    @Test
    void updateUser_ExpectedCorrect() {
        Long id = 2L;
        User userUpdateName = new User(null, "Name Update", null);

        UserDto afterUpdate = service.updateUser(userUpdateName, id);
        assertEquals("Name Update", afterUpdate.getName(), "Имя не обновилось");

        User userUpdateEmail = new User(null, null, "parrygotter@mail.com");
        UserDto afterUpdateEmail = service.updateUser(userUpdateEmail, id);

        assertEquals("parrygotter@mail.com", afterUpdateEmail.getEmail(), "Емейл не обновился");
    }

    @Test
    void updateUser_ExpectedNotFoundThrows() {
        User userUpdateWithDuplicateEmail = new User(null, null, "email@email.com");

        Throwable throwableNotFound = assertThrows(NotFoundException.class,
                () -> service.updateUser(userUpdateWithDuplicateEmail, 999L));
        assertEquals("User not found! User id: " + 999L,
                throwableNotFound.getMessage(), "Тест на проверку юзера при обновлении провален!");
    }

    @Test
    void updateUser_ExpectedEmailConflictException() {
        User userUpdateWithDuplicateEmail = new User(null, null, "email@email.com");

        Throwable throwableEmailConflict = assertThrows(EmailConflictException.class,
                () -> service.updateUser(userUpdateWithDuplicateEmail, 2L));
        assertEquals("This email '" + userUpdateWithDuplicateEmail.getEmail() + "' not unique, please check email",
                throwableEmailConflict.getMessage(), "Тест на проверку дубликата емейл при обновлении провален!");
    }

    @Test
    void getAllUser() {
        assertEquals(2, service.findAllUser().size(), "Кто-то не создался!!");
    }

    @Test
    void findUserById_ExpectedThrows() {
        Long id = 998L;
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> service.findUserById(id));
        assertEquals("User id: '" + id + "' not found, please check user id", throwable.getMessage(),
                "Мы кого то нашли 0_0");
    }

    @Test
    void deleteUser_ExpectedCorrect() {
        User user = new User(null, "Deleted", "deleted@delet.com");
        UserDto userDto = service.createNewUser(user);
        Long id = userDto.getId();

        assertEquals("Deleted", service.findUserById(id).getName(), "Юзер не создался");
        service.deleteUserById(id);
        Throwable throwable = assertThrows(NotFoundException.class,
                () -> service.findUserById(id));
        assertEquals("User id: '" + id + "' not found, please check user id", throwable.getMessage(),
                "Юзер под удаление существует!");

    }
}
