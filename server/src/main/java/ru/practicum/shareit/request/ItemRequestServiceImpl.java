package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectDataExeption;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.itemToShortDtoList;
import static ru.practicum.shareit.request.ItemRequestMapper.*;

/**
 * @author MR.k0F31n
 */
@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository repository;
    private final UserRepository users;
    private final ItemRepository items;

    @Transactional
    @Override
    public ItemRequestDto createNewItemRequest(ItemRequestInputDto input, Long userId) {
        final User user = getUserById(userId);
        final ItemRequest newRequest = dtoToObject(input);
        newRequest.setRequester(user);
        log.debug("Task - create new request. Input data: '{}'", input);
        return objectToDto(repository.save(newRequest));
    }

    @Override
    public List<ItemRequestWithAnswerDto> getAllItemRequestByUserIdWithAnswer(Long id, Pageable pageable) {
        getUserById(id);
        log.debug("Task - get all request by user owner request");
        final List<ItemRequestWithAnswerDto> allRequest = objectToDtoListWithAnswer(repository
                .findAllByRequesterIdOrderByCreatedDateDesc(id, pageable));
        for (ItemRequestWithAnswerDto request : allRequest) {
            setAnswerToItemRequest(request);
        }
        return allRequest;
    }

    @Override
    public List<ItemRequestWithAnswerDto> getAllRequest(Long userId, Pageable pageable) {
        getUserById(userId);
        log.debug("Task - get all request from all users");
        List<ItemRequestWithAnswerDto> result = objectToDtoListWithAnswer(repository
                .findAllByRequesterIdNotOrderByCreatedDateDesc(userId, pageable));
        for (ItemRequestWithAnswerDto request : result) {
            setAnswerToItemRequest(request);
        }
        return result;
    }

    @Override
    public ItemRequestWithAnswerDto getAllItemRequestFromOtherUsers(Long requestId, Long userId) {
        getUserById(userId);
        log.debug("Task - get Request by ID");
        final ItemRequestWithAnswerDto request = objectToDtoWithAnswer(getRequestById(requestId));
        setAnswerToItemRequest(request);
        return request;
    }

    private ItemRequest getRequestById(Long id) {
        log.debug("Task. Search by ID request. Id = '{}'", id);
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Item request not found"));
    }

    private User getUserById(Long id) {
        return users.findById(id).orElseThrow(() -> new IncorrectDataExeption("User not found"));
    }

    private void setAnswerToItemRequest(ItemRequestWithAnswerDto request) {
        request.setItems(itemToShortDtoList(items.findAllByRequestId(request.getId())));
    }
}
