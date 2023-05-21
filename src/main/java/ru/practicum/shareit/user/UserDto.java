package ru.practicum.shareit.user;

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
    @NotBlank(message = "Name is empty")
    private String name;
    @Email(message = "Email is not in the correct format")
    @NotBlank(message = "Email is empty")
    private String email;
}