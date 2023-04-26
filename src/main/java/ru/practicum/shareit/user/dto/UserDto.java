package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;
}