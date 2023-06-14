package ru.practicum.shareit.request;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface ItemRequestService {
    ItemRequestDto createNewItemRequest(ItemRequestInputDto input, Long userId);

    List<ItemRequestWithAnswerDto> getAllItemRequestByUserIdWithAnswer(Long id, Pageable pageable);

    List<ItemRequestWithAnswerDto> getAllRequest(Long id, Pageable pageable);

    ItemRequestWithAnswerDto getAllItemRequestFromOtherUsers(Long requestId, Long userId);
}
