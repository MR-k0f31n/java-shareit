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

    @Override
    public List<UserDto> findAllUser() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users.values()) {
            usersDto.add(RowMapper.toUserDto(user));
        }
        log.debug("Return Collection UsersDto, size collection: '{}'", usersDto.size());
        return usersDto;
    }

    @Override
    public UserDto createNewUser(User user) {
        Long id = getId();
        user.setId(id);
        if (checkEmail(user.getEmail())) {
            return null;
        }
        users.put(id, user);
        log.debug("User added successfully, user info: '{}'", users.get(id));
        return RowMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(User user, Long id) {
        user.setId(id);
        users.put(id, user);
        log.debug("User update successfully, user after update info: '{}'", users.get(id));
        return RowMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto findUserById(Long id) {
        return RowMapper.toUserDto(users.get(id));
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
        log.debug("User delete successfully, check user: '{}'", checkUser(id));
    }

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
