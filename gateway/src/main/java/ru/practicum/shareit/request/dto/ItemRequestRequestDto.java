package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author MR.k0F31N
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestRequestDto {
    @NotNull(message = "Request description is null")
    @NotBlank(message = "Request description is blank")
    private String description;
}
