package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService service;

    @Test
    void testCreateNewUser_correct() {
        UserDto userDto = new UserDto(1L, "Name1", "email@mail.com");

        UserDto user = service.createNewUser(userDto);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(userDto.getName()));
        assertThat(user.getEmail(), equalTo(userDto.getEmail()));
    }

    @Test
    void testGetAllUser_correct() {
        List<UserDto> users = new ArrayList<>();
        users.add(service.createNewUser(new UserDto(1L, "name1", "emai1@mail.com")));
        users.add(service.createNewUser(new UserDto(2L, "name2", "emai2@mail.com")));
        users.add(service.createNewUser(new UserDto(3L, "name3", "emai3@mail.com")));
        users.add(service.createNewUser(new UserDto(4L, "name4", "emai4@mail.com")));

        List<UserDto> usersDto = service.findAllUser();

        Assertions.assertNotNull(usersDto, "Юзеров нет");
        Assertions.assertEquals(4, usersDto.size(), "Количесвто юзеров не совпадает");
    }

    @Test
    void testFindById_correct() {
        UserDto user = new UserDto(1L, "name1", "emai1@mail.com");

        service.createNewUser(user);

        UserDto foundUser = service.findUserById(user.getId());

        Assertions.assertNotNull(foundUser, "Пользователь пуст");
        Assertions.assertEquals(user.getId(), foundUser.getId(), "Ид не совпадает");
        Assertions.assertEquals(user.getName(), foundUser.getName(), "имя не совпадает");
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail(), "емейл не совпадает");
    }

    @Test
    void testFindById_errorNotFound() {
        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> service.findUserById(99L));

        Assertions.assertEquals("User id: '99' not found, please check user id", exception.getMessage());
    }
}