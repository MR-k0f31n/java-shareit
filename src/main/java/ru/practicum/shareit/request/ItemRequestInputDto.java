package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author MR.k0F31n
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestInputDto {
    @NotNull(message = "Request description is null")
    @NotBlank(message = "Request description is blank")
    private String description;
}
