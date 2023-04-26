/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * @return All users, format DTO
     */
    List<UserDto> findAllUser();

    /**
     * Be sure to check email for uniqueness
     *
     * @param user object
     * @return User format Dto
     */
    UserDto createNewUser(User user);

    /**
     * Be sure to check email for uniqueness
     *
     * @param user object
     * @return User format Dto
     */
    UserDto updateUser(User user, Long id);

    /**
     * @param id Long id User
     * @return User format Dto
     */
    UserDto findUserById(Long id);

    /**
     * @param id Long id User
     */
    void deleteUserById(Long id);

    /**
     * @param id Long id User
     * @return boolean check User in collection
     */
    boolean checkUser(Long id);
}
