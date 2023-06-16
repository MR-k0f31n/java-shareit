package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@Slf4j
@Validated
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsersCollection() {
        log.info("Get all users collection");
        return userClient.getAllUserCollection();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id) {
        log.info("Get user by id = {}", id);
        return userClient.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createNewUser(@Valid @RequestBody UserRequestDto userDto) {
        log.info("Create new user {}", userDto);
        return userClient.createNewUser(userDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody UserRequestDto userDto, @PathVariable long id) {
        log.info("Update user id={}, update {}", id, userDto);
        return userClient.updateUser(userDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("Try delete user id={}", id);
        return userClient.deleteUser(id);
    }
}
