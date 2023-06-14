package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author MR.k0F31n
 */
@Getter
@AllArgsConstructor
public class ItemInputDto {
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
}
