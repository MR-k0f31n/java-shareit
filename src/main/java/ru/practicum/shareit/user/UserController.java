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
        log.debug("Endpoint request: 'GET /users'");
        return service.findAllUser();
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        log.debug("Endpoint request: 'GET /users/{id}'");
        return service.findUserById(id);
    }

    @PostMapping
    public UserDto createUser(@Valid @RequestBody User user) {
        log.debug("Endpoint request: 'POST /users'");
        return service.createNewUser(user);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@Valid @RequestBody User user, @PathVariable Long id) {
        log.debug("Endpoint request: 'PUT /users/{id}'");
        return service.updateUser(user, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.debug("Endpoint request: 'DELETE /users/{id}'");
        service.deleteUserById(id);
    }
}
