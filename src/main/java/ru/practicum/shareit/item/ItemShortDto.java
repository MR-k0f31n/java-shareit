package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author MR.k0F31n
 */
@Data
@AllArgsConstructor
public class ItemShortDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long ownerId;
    private Long requestId;
}
