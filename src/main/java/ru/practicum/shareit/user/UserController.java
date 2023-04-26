/**
 * @author MR.k0F31n
 */
package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Slf4j
public class UserController {
    private UserService service;
    @GetMapping
    public List<UserDto> getAllUser() {
        return service.findAllUser();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return service.findUserById(id);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        return service.createNewUser(user);
    }

    @PostMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody User user) {
        return service.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
