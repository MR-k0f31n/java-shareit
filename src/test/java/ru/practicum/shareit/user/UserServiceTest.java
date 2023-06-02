package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * @author MR.k0F31n
 */
public class UserServiceTest {
    UserRepository userRepository;
    UserService userService;
    ItemRepository items;

    @BeforeEach
    void beforeEach() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository, items);
    }

    @Test
    void testGetAllUser_correct() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "name1", "emai1@mail.com"));
        users.add(new User(2L, "name2", "emai2@mail.com"));
        users.add(new User(3L, "name3", "emai3@mail.com"));
        users.add(new User(4L, "name4", "emai4@mail.com"));
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> usersDto = userService.findAllUser();

        Assertions.assertNotNull(usersDto, "Юзеров нет");
        Assertions.assertEquals(4, usersDto.size(), "Количесвто юзеров не совпадает");
    }

    @Test
    void testFindById_correct() {
        User user = new User(1L, "name1", "emai1@mail.com");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto foundUser = userService.findUserById(user.getId());

        Assertions.assertNotNull(foundUser, "Пользователь пуст");
        Assertions.assertEquals(user.getId(), foundUser.getId(), "Ид не совпадает");
        Assertions.assertEquals(user.getName(), foundUser.getName(), "имя не совпадает");
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail(), "емейл не совпадает");
    }

    @Test
    void testFindById_errorNotFound() {
        when(userRepository.findById(anyLong())).thenThrow(new NotFoundException("User not found"));

        Exception exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.findUserById(99L));

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testCreateNewUser_correct() {
        User user = new User(1L, "name1", "emai1@mail.com");
        UserDto userDto = new UserDto(null, "name1", "emai1@mail.com");

        when(userRepository.save(any())).thenReturn(user);

        UserDto User = userService.createNewUser(userDto);

        Assertions.assertNotNull(User, "пользователь не создался");
        Assertions.assertEquals(user.getId(), User.getId(), "Неверный ид");
        Assertions.assertEquals(user.getName(), User.getName(), "Неверный имя");
        Assertions.assertEquals(user.getEmail(), User.getEmail(), "Неверный емейл");
    }
}
