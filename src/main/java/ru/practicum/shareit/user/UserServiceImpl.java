package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.user.UserDtoMapper.toUser;
import static ru.practicum.shareit.user.UserDtoMapper.toUserDto;

/**
 * @author MR.k0F31n
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAllUser() {
        log.debug("Task get all users");
        List<UserDto> itemsDto = new ArrayList<>();
        for (User user : repository.findAllUser()) {
            itemsDto.add(toUserDto(user));
        }
        return itemsDto;
    }

    @Override
    public UserDto createNewUser(UserDto userDto) {
        log.warn("Task create new user, user info: '{}'", userDto);
        if (repository.isEmailExist(userDto.getEmail())) {
            throw new EmailConflictException("This email '" + userDto.getEmail() + "' not unique, please check email");
        }
        User user = toUser(userDto);
        return toUserDto(repository.createNewUser(user));
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {
        log.warn("Task update user, user info: '{}'", userDto);
        String email = userDto.getEmail();
        String name = userDto.getName();
        User userForUpdate = toUser(findUserById(id));
        if (name != null) {
            userForUpdate.setName(name);
        }
        if (email != null) {
            if (repository.isEmailExist(email)) {
                if (!userForUpdate.getEmail().equals(email)) {
                    throw new EmailConflictException("This email '" + userDto.getEmail() + "' not unique, " +
                            "please check email");
                }
            }
            userForUpdate.setEmail(email);
        }
        return toUserDto(repository.updateUser(userForUpdate, id));
    }

    @Override
    public UserDto findUserById(Long id) {
        log.warn("Task find user by id, user id: '{}'", id);
        try {
            return toUserDto(repository.findUserById(id));
        } catch (NullPointerException exception) {
            throw new NotFoundException("User id: '" + id + "' not found, please check user id");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        log.warn("try delete user by id, user id: '{}'", id);
        repository.deleteUserById(id);
    }
}
