package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTestMock {
    final UserDto user1 = new UserDto(1L, "name1", "emai1@mail.com");
    final UserDto user2 = new UserDto(2L, "name2", "emai2@mail.com");
    final UserDto user3 = new UserDto(3L, "name3", "email3@mail.com");
    final UserDto user4 = new UserDto(4L, "name4", "email4@mail.com");
    @MockBean
    private final UserService service;

    @Test
    void createNewUser_returnUserDto() {
        when(service.createNewUser(any(UserDto.class))).thenReturn(user1);

        UserDto user = service.createNewUser(user1);

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(user1.getName()));
        assertThat(user.getEmail(), equalTo(user1.getEmail()));
    }

    @Test
    void getAllUsers_returnListDto_existLength4() {
        List<UserDto> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        when(service.findAllUser()).thenReturn(users);

        List<UserDto> usersDto = service.findAllUser();

        assertNotNull(usersDto, "Юзеров нет");
        assertEquals(4, usersDto.size(), "Количесвто юзеров не совпадает");
    }

    @Test
    void searchUserById_returnUserOne_existUserOne() {
        UserDto user = new UserDto(1L, "name1", "emai1@mail.com");

        when(service.findUserById(anyLong())).thenReturn(user);

        UserDto foundUser = service.findUserById(user.getId());

        assertNotNull(foundUser, "Пользователь пуст");
        assertEquals(user.getId(), foundUser.getId(), "Ид не совпадает");
        assertEquals(user.getName(), foundUser.getName(), "имя не совпадает");
        assertEquals(user.getEmail(), foundUser.getEmail(), "емейл не совпадает");
    }

    @Test
    void searchUserById_expectedThrow_user99notExist() {
        when(service.findUserById(anyLong())).thenThrow(new NotFoundException("User not found"));

        assertThrows(NotFoundException.class, () -> service.findUserById(99L), "Юзер 99 обнаружен");
    }

    @Test
    void updateUserWithEmailWrong_emailNull_expectedError() {
        when(service.updateUser(any(UserDto.class), anyLong())).thenThrow(ValidatorException.class);

        UserDto user = user1;
        user.setEmail(null);

        assertThrows(ValidatorException.class, () -> service.updateUser(user, 1L));
    }

    @Test
    void updateUserWithEmailWrong_emailSpace_expectedError() {
        when(service.updateUser(any(UserDto.class), anyLong())).thenThrow(ValidatorException.class);

        UserDto user = user1;
        user.setEmail(" ");

        assertThrows(ValidatorException.class, () -> service.updateUser(user, 1L));
    }

    @Test
    void updateUserWithEmailWrong_emailEmpty_expectedError() {
        when(service.updateUser(any(UserDto.class), anyLong())).thenThrow(ValidatorException.class);

        UserDto user = user1;
        user.setEmail("");

        assertThrows(ValidatorException.class, () -> service.updateUser(user, 1L));
    }

    @Test
    void updateUserWithEmailWrong_emailWithSpace_expectedError() {
        when(service.updateUser(any(UserDto.class), anyLong())).thenThrow(ValidatorException.class);

        UserDto user = user1;
        user.setEmail("email @mail.com");

        assertThrows(ValidatorException.class, () -> service.updateUser(user, 1L));
    }

    @Test
    void updateUser_userNotExist_expectedError() {
        when(service.updateUser(any(UserDto.class), anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateUser(user1, 99L));
    }

    @Test
    void updateUserName_returnUSerDto() {
        UserDto updateUser = user1;
        updateUser.setName("Update name");

        when(service.updateUser(any(UserDto.class), anyLong())).thenReturn(updateUser);

        assertEquals(updateUser.getName(), service.updateUser(updateUser, 1L).getName(), "Name not update");
    }
}