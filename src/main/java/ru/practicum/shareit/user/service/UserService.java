package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUser();

    UserDto createNewUser(User user);

    UserDto updateUser(User user);

    UserDto findUserById(Long id);

    void deleteUserById(Long id);
}
