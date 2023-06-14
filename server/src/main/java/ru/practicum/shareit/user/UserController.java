package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public UserDto createUser(@RequestBody UserDto userDto) {
        log.debug("Endpoint request: 'POST /users'");
        return service.createNewUser(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        log.debug("Endpoint request: 'PUT /users/{userId}'");
        return service.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.debug("Endpoint request: 'DELETE /users/{id}'");
        service.deleteUserById(id);
    }
}
