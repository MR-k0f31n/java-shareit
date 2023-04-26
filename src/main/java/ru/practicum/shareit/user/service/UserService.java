package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<User> findAllUser();

    User createNewUser(User user);

    User updateUser(User user);

    User findUserById(Long id);

    void deleteUserById(Long id);
}
