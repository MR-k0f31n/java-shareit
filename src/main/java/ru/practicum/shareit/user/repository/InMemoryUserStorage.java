/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.util.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class InMemoryUserStorage implements UserRepository {
    /**
     * All this is temporary and all this will pass
     */
    private final Map<Long, User> users;

    /**
     * @return All users, format DTO
     */
    @Override
    public List<UserDto> findAllUser() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users.values()) {
            usersDto.add(RowMapper.toUserDto(user));
        }
        log.debug("Return Collection UsersDto, size collection: '{}'", usersDto.size());
        return usersDto;
    }

    /**
     * Be sure to check email for uniqueness
     *
     * @param user
     * @return User format Dto
     */
    @Override
    public UserDto createNewUser(User user) {
        user.setId(getId());
        if (checkEmail(user.getEmail())) {
            return null;
        }
        users.put(user.getId(), user);
        log.debug("User added successfully, user info: '{}'", users.get(user.getId()));
        return RowMapper.toUserDto(user);
    }

    /**
     * Be sure to check email for uniqueness
     *
     * @param user
     * @return User format Dto
     */
    @Override
    public UserDto updateUser(User user) {
        if (checkEmail(user.getEmail())) {
            return null;
        }
        users.put(user.getId(), user);
        log.debug("User update successfully, user after update info: '{}'", users.get(user.getId()));
        return RowMapper.toUserDto(users.get(user.getId()));
    }

    /**
     * This is temporary
     *
     * @param id
     * @return User format Dto
     */
    @Override
    public UserDto findUserById(Long id) {
        return RowMapper.toUserDto(users.get(id));
    }

    /**
     * @param id
     */
    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
        log.debug("User delete successfully, id deleted user: '{}'", id);
    }

    /**
     * @param id
     */
    @Override
    public boolean checkUser(Long id) {
        if (users.containsKey(id)) {
            log.debug("User detected id '{}'", id);
            return true;
        }
        log.debug("User not Found id '{}'", id);
        return false;
    }

    private boolean checkEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().toLowerCase().equals(email)) {
                log.debug("User email not unique! email '{}'", email);
                return true;
            }
        }
        log.debug("User email unique! email '{}'", email);
        return false;
    }

    private long getId() {
        long lastId = users.values().stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        log.debug("Give a new id to the user: '{}'", lastId + 1);
        return lastId + 1;
    }
}
