package ru.practicum.shareit.request;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRequestService {
    ItemRequestDto createNewItemRequest(ItemRequestInputDto input, Long userId);

    List<ItemRequestWithAnswerDto> getAllItemRequestByUserIdWithAnswer(Long id, Integer from, Integer size);

    List<ItemRequestWithAnswerDto> getAllRequest(Long id, Integer from, Integer size);

    ItemRequestWithAnswerDto getAllItemRequestFromOtherUsers(Long requestId, Long userId);
}
