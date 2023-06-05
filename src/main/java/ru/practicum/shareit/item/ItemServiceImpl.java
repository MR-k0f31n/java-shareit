package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.item.ItemMapper.dtoToItem;
import static ru.practicum.shareit.item.ItemMapper.itemToDto;
import static ru.practicum.shareit.item.comment.CommentMapper.*;
import static ru.practicum.shareit.user.UserMapper.dtoToUser;

/**
 * @author MR.k0F31n
 */
@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private ItemRepository repository;
    private UserService userService;
    private BookingRepository bookingRepository;
    private CommentRepository commentRepository;
    private ItemRequestRepository requests;

    @Override
    public List<ItemDto> getAllItemsByOwner(Long id, Integer from, Integer size) {
        log.debug("Task get all items by owner");
        userService.findUserById(id);
        Pageable pageable = PageRequest.of(from / size, size);
        List<ItemDto> itemDtoList = itemToDto(repository.findAllByOwnerId(id, pageable));
        for (ItemDto itemDto : itemDtoList) {
            setNextAndLastDateBooking(itemDto);
            setComments(itemDto);
        }
        return itemDtoList;
    }

    @Transactional
    @Override
    public ItemDto createNewItem(ItemInputDto itemDto, Long ownerId) {
        log.warn("Task create new item, item info: '{}'", itemDto);
        final User user = dtoToUser(userService.findUserById(ownerId));
        final Item item = dtoToItem(itemDto);
        item.setOwner(user);
        item.setRequest(null);
        if (itemDto.getRequestId() != null) {
            item.setRequest(requests.findById(itemDto.getRequestId()).orElseThrow(() -> new NotFoundException("Request not found")));
        }
        final Item itemBeforeSave = repository.save(item);
        return itemToDto(itemBeforeSave);
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, Long id, Long ownerId) {
        log.warn("Task update item, item info: " + itemDto);
        userService.findUserById(ownerId);
        String name = itemDto.getName();
        String description = itemDto.getDescription();
        Boolean available = itemDto.getAvailable();
        if (name == null && description == null && available == null) {
            return null;
        }
        final Item itemForUpdate = getItemById(id);
        if (!itemForUpdate.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        if (name != null && !name.isBlank()) {
            itemForUpdate.setName(name);
        }
        if (description != null && !description.isBlank()) {
            itemForUpdate.setDescription(description);
        }
        if (available != null) {
            itemForUpdate.setAvailable(available);
        }
        return itemToDto(repository.save(itemForUpdate));
    }

    @Override
    public ItemDto getItemDtoById(Long id, Long userId) {
        log.warn("Task get item by id, item id: '{}'", id);
        ItemDto returnItem = itemToDto(getItemById(id));
        if (returnItem.getOwner().getId() == userId.longValue()) {
            setNextAndLastDateBooking(returnItem);
        } else {
            returnItem.setLastBooking(null);
            returnItem.setNextBooking(null);
        }
        setComments(returnItem);
        return returnItem;
    }

    @Transactional
    @Override
    public void deleteItem(Long id, Long ownerId) {
        userService.findUserById(ownerId);
        if (!getItemById(id).getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Unable to update item , user does not have such item");
        }
        log.warn("try delete item by id, item id: '{}'", id);
        repository.deleteById(id);
    }

    @Override
    public List<ItemDto> searchItem(String requestSearch, Integer from, Integer size) {
        if (requestSearch.isEmpty() || requestSearch.isBlank()) {
            return Collections.emptyList();
        }
        Pageable pageable = PageRequest.of(from / size, size);
        return itemToDto(
                repository.findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAndAvailableTrue(
                        requestSearch, requestSearch, pageable));
    }

    @Transactional
    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        log.debug("Task add comments");
        if (commentInputDto.getText().isEmpty()) {
            throw new ValidatorException("Text comment is empty");
        }
        if (!bookingRepository.existsByBookerIdAndItemIdAndEndRentBefore(userId, itemId,
                LocalDateTime.now())) {
            throw new ValidatorException("User not rent item or rent not finished");
        }
        final User author = dtoToUser(userService.findUserById(userId));
        final Item item = getItemById(itemId);
        final Comment newComment = toComment(commentInputDto);
        newComment.setItem(item);
        newComment.setAuthor(author);
        log.debug("Comments add info: " + newComment);
        return toCommentDto(commentRepository.save(newComment));
    }

    private Item getItemById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Item not found! Item id: " + id));
    }

    private void setNextAndLastDateBooking(ItemDto item) {
        item.setNextBooking(bookingRepository
                .findFirstByItemIdAndStartRentAfterAndStatusOrderByStartRentAsc(item.getId(),
                        LocalDateTime.now(),
                        Status.APPROVED)
                .map(BookingMapper::bookingToShortDto)
                .orElse(null));

        item.setLastBooking(bookingRepository
                .findFirstByItemIdAndStartRentBeforeAndStatusOrderByStartRentDesc(item.getId(),
                        LocalDateTime.now(),
                        Status.APPROVED)
                .map(BookingMapper::bookingToShortDto)
                .orElse(null));
    }

    private void setComments(ItemDto item) {
        item.setComments(toCommentDtoList(commentRepository.findAllByItemId(item.getId())));
    }
}
