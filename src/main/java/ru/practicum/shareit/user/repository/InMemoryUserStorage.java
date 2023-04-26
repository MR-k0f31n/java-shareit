/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.practicum.shareit.util.RowMapper.toUserDto;

@Component
@Slf4j
public class InMemoryUserStorage implements UserRepository {
    /**
     * All this is temporary and all this will pass
     */
    private final Map<Long, User> users = new HashMap<>();
    private Long currencyId = 1L;

    @Override
    public List<UserDto> findAllUser() {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users.values()) {
            usersDto.add(toUserDto(user));
        }
        log.debug("Return Collection UsersDto, size collection: '{}'", usersDto.size());
        return usersDto;
    }

    @Override
    public UserDto createNewUser(User user) {
        if (checkEmail(user.getEmail())) {
            return null;
        }
        Long id = currencyId++;
        user.setId(id);
        users.put(id, user);
        log.debug("User added successfully, user info: '{}'", toUserDto(users.get(id)));
        return toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(User user, Long id) {
        User forUpdateUser = users.get(id);
        String name = user.getName();
        String email = user.getEmail();
        if (email != null) {
            if (checkEmail(email)) {
                if (!forUpdateUser.getEmail().equals(email)) {
                    return null;
                }
            }
            forUpdateUser.setEmail(email);
        }
        if (name != null) {
            forUpdateUser.setName(name);
        }
        users.put(id, forUpdateUser);
        log.debug("User update successfully, user after update info: '{}'", toUserDto(users.get(id)));
        return toUserDto(users.get(id));
    }

    @Override
    public UserDto findUserById(Long id) {
        log.debug("find user by id '{}' user info", id);
        return toUserDto(users.get(id));
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
            if (user.getEmail().equals(email)) {
                log.debug("User email not unique! email '{}'", email);
                return true;
            }
        }
        log.debug("User email unique! email '{}'", email);
        return false;
    }
}
