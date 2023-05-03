/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * @return All users, format DTO
     */
    List<User> findAllUser();

    /**
     * Be sure to check email for uniqueness
     *
     * @param user object
     * @return User object
     */
    User createNewUser(User user);

    /**
     * Be sure to check email for uniqueness
     *
     * @param user object
     * @return User object
     */
    User updateUser(User user, Long id);

    /**
     * @param id Long id User
     * @return User object
     */
    User findUserById(Long id);

    /**
     * @param id Long id User
     */
    void deleteUserById(Long id);


    /**
     * @param email Email format string
     * @return boolean check email for unique
     */
    boolean isEmailExist(String email);
}
