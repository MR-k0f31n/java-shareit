/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailConflictExeption;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public List<UserDto> findAllUser() {
        log.debug("Task getAllUsers");
        return repository.findAllUser();
    }

    @Override
    public UserDto createNewUser(User user) {
        log.warn("Task createNewUser, user info: '{}'", user);
        UserDto userDto = repository.createNewUser(user);
        if (userDto == null) {
            throw new EmailConflictExeption("This email '" + user.getEmail() + "' not unique, please check email");
        }
        return userDto;
    }

    @Override
    public UserDto updateUser(User user) {
        log.warn("Task updateUser, user info: '{}'", user);
        UserDto userDto = repository.updateUser(user);
        if(!repository.checkUser(user.getId())){
            throw new NotFoundException("User not found! User id: " + user.getId());
        }
        if (userDto == null) {
            throw new EmailConflictExeption("This email '" + user.getEmail() + "' not unique, please check email");
        }
        log.trace("Task updateUser, successfully. User info after update: '{}'", userDto);
        return userDto;
    }

    @Override
    public UserDto findUserById(Long id) {
        log.warn("Task findUserById, user id: '{}'", id);
        UserDto userDto = repository.findUserById(id);
        if (userDto == null) {
            throw new NotFoundException("User id: '" + id + "' not found, please check user id");
        }
        log.trace("Task findUserById, successfully");
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
        log.warn("try deleteUserById, user id: '{}'", id);
        repository.deleteUserById(id);
    }
}
