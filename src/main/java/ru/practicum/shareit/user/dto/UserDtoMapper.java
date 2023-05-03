package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * @author MR.k0F31n
 */
public class UserDtoMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
    }
}
