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

    private final List<User> users;

    @Override
    public List<UserDto> findAllUser() {
        return new ArrayList<>(users.forEach(User::Row););
    }

    @Override
    public UserDto createNewUser(User user) {
        user.setId(getId());
        users.add(user);
        return RowMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(User user) {
        return null;
    }

    @Override
    public UserDto findUserById(Long id) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }

    private long getId() {
        long lastId = users.stream()
                .mapToLong(User::getId)
                .max()
                .orElse(0);
        return lastId + 1;
    }
}
