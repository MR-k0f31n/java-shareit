/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    /**
     * @return All users format DTO
     */
    List<UserDto> findAllUser();

    /**
     * Be sure to check email for uniqueness
     *
     * @param user object
     * @return User format DTO
     */
    UserDto createNewUser(User user);

    /**
     * Be sure to check email for uniqueness and contains user
     *
     * @param user object
     * @return User format DTO
     */
    UserDto updateUser(User user, Long id);

    /**
     * @param id Long id user
     * @return User format DTO
     */
    UserDto findUserById(Long id);

    void deleteUserById(Long id);
}
