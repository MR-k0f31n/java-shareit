package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.ItemShortDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author MR.k0F31n
 */
@Data
@AllArgsConstructor
public class ItemRequestWithAnswerDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemShortDto> items;
}
