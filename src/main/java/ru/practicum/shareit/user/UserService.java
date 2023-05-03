/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    /**
     * @return All users format DTO
     */
    List<UserDto> findAllUser();

    /**
     * Be sure to check email for uniqueness
     *
     * @param userDto object
     * @return User format DTO
     */
    UserDto createNewUser(UserDto userDto);

    /**
     * Be sure to check email for uniqueness and contains user
     *
     * @param userDto object
     * @return User format DTO
     */
    UserDto updateUser(UserDto userDto, Long id);

    /**
     * @param id Long id user
     * @return User format DTO
     */
    UserDto findUserById(Long id);

    /**
     * @param id Long id user
     */
    void deleteUserById(Long id);
}
