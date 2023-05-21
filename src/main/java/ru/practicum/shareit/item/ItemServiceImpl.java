package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentInputDto;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ru.practicum.shareit.comment.CommentMapper.toComment;
import static ru.practicum.shareit.comment.CommentMapper.toCommentDto;
import static ru.practicum.shareit.item.ItemMapper.*;
import static ru.practicum.shareit.user.UserMapper.toUser;

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

    @Override
    public List<ItemDto> getAllItemsByOwner(Long id) {
        log.debug("Task get all items by owner");
        userService.findUserById(id);
        List<ItemDto> itemDtoList = toItemDtoList(repository.getItemsByOwnerId(id));
        for(ItemDto itemDto : itemDtoList) {
            setNextLastDateBooking(itemDto);
            itemDto.setComments(commentRepository.findAllByItemId(id));
        }
        return toItemDtoList(repository.getItemsByOwnerId(id));
    }

    @Override
    public ItemDto createNewItem(ItemDto itemDto, Long ownerId) {
        log.warn("Task create new item, item info: '{}'", itemDto);
        userService.findUserById(ownerId);
        final Item item = toItem(itemDto, ownerId);
        item.getOwner().setId(ownerId);
        return toItemDto(repository.save(item));
    }

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
        return toItemDto(repository.save(itemForUpdate));
    }

    @Override
    public ItemDto getItemDtoById(Long id) {
        log.warn("Task get item by id, item id: '{}'", id);
        ItemDto returnItem = toItemDto(getItemById(id));
        returnItem = setNextLastDateBooking(returnItem);
        returnItem.setComments(commentRepository.findAllByItemId(id));
        return returnItem;
    }

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
    public List<ItemDto> searchItem(String requestSearch) {
        if (requestSearch.isEmpty() || requestSearch.isBlank()) {
            return Collections.emptyList();
        }
        return toItemDtoList(repository.
                findAllByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndAndAvailableTrue(requestSearch,
                        requestSearch));
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, CommentInputDto commentInputDto) {
        if (commentInputDto.getText().isEmpty()) {
            throw new ValidatorException("Text comment is empty");
        }
        final Booking booking = bookingRepository.findByBookerIdAndItemIdAndEndRentAfterAndStatus(userId, itemId,
                LocalDateTime.now(), Status.APPROVED);
        if(booking == null) {
            throw new ValidatorException("User not rent item or rent not finished");
        }
        final Comment comment = commentRepository.findByAuthorIdAndItemId(userId, itemId);
        if (comment != null) {
            throw new ValidatorException("Thanks you comment");
        }
        final User author = toUser(userService.findUserById(userId));
        final Item item = getItemById(itemId);
        final Comment newComment = toComment(commentInputDto);
        newComment.setItem(item);
        newComment.setAuthor(author);
        return toCommentDto(commentRepository.save(newComment));
    }

    private Item getItemById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Item not found! Item id: " + id));
    }

    private ItemDto setNextLastDateBooking(ItemDto item) {
        item.setLastBooking(bookingRepository.findFirstByItemIdAndStartRentBeforeAndStatusOrderByEndRentDesc
                (item.getId(), LocalDateTime.now(), Status.APPROVED));
        item.setNextBooking(bookingRepository.findFirstByItemIdAndStartRentAfterAndStatusOrderByEndRentAsc
                (item.getId(), LocalDateTime.now(), Status.APPROVED));
        return item;
    }
}
