package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
}