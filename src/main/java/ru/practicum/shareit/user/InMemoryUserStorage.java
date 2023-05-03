/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserRepository {
    /**
     * All this is temporary and all this will pass
     */
    private final Map<Long, User> users = new HashMap<>();
    private Long currencyId = 1L;

    @Override
    public List<User> findAllUser() {
        log.debug("Return Collection UsersDto, size collection: '{}'", users.size());
        return List.copyOf(users.values());
    }

    @Override
    public User createNewUser(User user) {
        Long id = currencyId++;
        user.setId(id);
        users.put(id, user);
        log.debug("User added successfully, user info: '{}'", users.get(id));
        return users.get(id);
    }

    @Override
    public User updateUser(User user, Long id) {
        users.put(id, user);
        log.debug("User update successfully, user after update info: '{}'", users.get(id));
        return users.get(id);
    }

    @Override
    public User findUserById(Long id) {
        log.debug("find user by id '{}' user info", users.get(id));
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        users.remove(id);
        log.debug("User delete successfully, check id='{}' is user exist: '{}'", id, users.containsKey(id));
    }

    @Override
    public boolean isEmailExist(String email) {
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
