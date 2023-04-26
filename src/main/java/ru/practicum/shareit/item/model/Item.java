package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class Item {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private boolean available;
    @NotNull
    private Long ownerId;
    private boolean request;
}
