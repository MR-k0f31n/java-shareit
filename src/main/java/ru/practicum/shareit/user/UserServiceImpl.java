package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.user.UserMapper.*;

/**
 * @author MR.k0F31n
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ItemRepository itemRepository;

    @Override
    public List<UserDto> findAllUser() {
        log.debug("Task get all users");
        return toUserDtoList(repository.findAll());
    }

    @Transactional
    @Override
    public UserDto createNewUser(UserDto userDto) {
        log.warn("Task create new user, user info: '{}'", userDto);
        User user = repository.save(toUser(userDto));
        return toUserDto(user);
    }

    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        log.warn("Task update user, user info: '{}'", userDto);
        String email = userDto.getEmail();
        String name = userDto.getName();
        if (name == null && email == null) {
            return null;
        }
        final User userForUpdate = repository.findById(id).orElseThrow
                (() -> new NotFoundException("User id: '" + id + "' not found, please check user id"));
        if (name != null && !name.isBlank()) {
            userForUpdate.setName(name);
            log.trace("name update on ='{}'", name);
        }
        if (email != null && !email.isBlank()) {
            userForUpdate.setEmail(email);
            log.trace("email update on ='{}'", email);
        }
        try {
            log.warn("Task update user, user info after update: '{}'", userForUpdate);
            return toUserDto(repository.save(userForUpdate));
        } catch (Exception exception) {
            throw new EmailConflictException("Email exist '" + email + "'");
        }
    }

    @Override
    public UserDto findUserById(Long id) {
        log.warn("Task find user by id, user id: '{}'", id);
        User user = repository.findById(id).orElseThrow
                (() -> new NotFoundException("User id: '" + id + "' not found, please check user id"));
        return toUserDto(user);
    }

    @Transactional
    @Override
    public void deleteUserById(Long id) {
        findUserById(id);
        for (Item item : itemRepository.getItemsByOwnerId(id)) {
            itemRepository.deleteById(item.getId());
        }
        log.warn("try delete user by id, user id: '{}'", id);
        repository.deleteById(id);
    }
}
