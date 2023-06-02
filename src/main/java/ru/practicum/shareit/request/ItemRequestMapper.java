package ru.practicum.shareit.request;

import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class ItemRequestMapper {
    public static ItemRequest dtoToObject(ItemRequestDto dto) {
        return new ItemRequest(
                dto.getId(),
                dto.getDescription(),
                new User(),
                dto.getCreated()
        );
    }

    public static ItemRequest dtoToObject(ItemRequestInputDto dto) {
        return new ItemRequest(
                null,
                dto.getDescription(),
                new User(),
                LocalDateTime.now()
        );
    }

    public static ItemRequestDto objectToDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getRequester().getId(),
                request.getCreatedDate()
        );
    }

    public static ItemRequestWithAnswerDto objectToDtoWithAnswer(ItemRequest request) {
        return new ItemRequestWithAnswerDto(
                request.getId(),
                request.getDescription(),
                request.getCreatedDate(),
                new ArrayList<>()
        );
    }

    public static List<ItemRequestDto> objectToDtoList(Iterable<ItemRequest> iterable) {
        List<ItemRequestDto> result = new ArrayList<>();
        for (ItemRequest item : iterable) {
            result.add(objectToDto(item));
        }
        return result;
    }

    public static List<ItemRequestWithAnswerDto> objectToDtoListWithAnswer(Iterable<ItemRequest> iterable) {
        List<ItemRequestWithAnswerDto> result = new ArrayList<>();
        for (ItemRequest item : iterable) {
            result.add(objectToDtoWithAnswer(item));
        }
        return result;
    }
}
