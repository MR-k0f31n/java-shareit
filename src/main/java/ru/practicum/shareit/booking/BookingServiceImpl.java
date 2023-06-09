package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.UnsupportedStatus;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.BookingMapper.bookingToDto;
import static ru.practicum.shareit.booking.BookingMapper.dtoToBooking;

/**
 * @author MR.k0F31n
 */
@Service
@Slf4j
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public BookingDto createNewBooking(BookingInputDto bookingInputDTODto, Long userId) {
        log.debug("Task create new booking info: " + bookingInputDTODto + " userID: " + userId);
        Long itemId = bookingInputDTODto.getItemId();
        if (bookingInputDTODto.getStart().isAfter(bookingInputDTODto.getEnd()) ||
                bookingInputDTODto.getEnd().isEqual(bookingInputDTODto.getStart())) {
            log.debug("Booking start time before or equals booking end time. Time info: start - " +
                    bookingInputDTODto.getStart() + " end - " + bookingInputDTODto.getEnd());
            throw new ValidatorException("Booking start time before or equals booking end time");
        }
        final Item item = getItemById(itemId);
        if (userId == item.getOwner().getId().longValue()) {
            log.debug("don't can rent own items exeption. item info: " + item);
            throw new NotFoundException("you don't can rent own items");
        }
        if (!item.getAvailable()) {
            log.debug("Item not available item info: " + item);
            throw new ValidatorException("Item not available");
        }
        final User booker = getUserById(userId);

        final Booking booking = dtoToBooking(bookingInputDTODto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        log.debug("Task create booking successfully info: " + booking);
        return bookingToDto(repository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto setStatusBooking(Long bookingId, Long userId, Boolean isApproved) {
        log.debug("Task set status booking.");
        final Booking booking = getBookingById(bookingId);
        if (!booking.getStatus().equals(Status.WAITING)) {
            log.debug("Booking '{'" + booking + "'}' status not waiting");
            throw new ValidatorException("Booking id '{ " + bookingId + " }' status not waiting");
        }
        final Item item = booking.getItem();
        final User owner = item.getOwner();
        if (!(owner.getId().equals(userId))) {
            log.debug("Exeption: Not owner reader. getting owner info: " + owner + " getting item owner id = " +
                    item.getOwner().getId());
            throw new NotFoundException("Not owner reader");
        }
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        final BookingDto bookingDtoAfterUpdate = bookingToDto(repository.save(booking));
        log.debug("Booking change status. Booking info after update: " + bookingDtoAfterUpdate);
        return bookingDtoAfterUpdate;
    }

    @Override
    public List<BookingDto> getAllBookingByStatusFromOwner(Long userId, String status, Pageable pageable) {
        getUserById(userId);
        switch (status) {
            case "ALL":
                return bookingToDto(repository.findAllByItemOwnerIdOrderByStartRentDesc(userId, pageable));
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingToDto(repository.findAllByItemOwnerIdAndStartRentBeforeAndEndRentAfterOrderByStartRent(
                        userId, now, now, pageable));
            case "PAST":
                return bookingToDto(repository.findAllByItemOwnerIdAndEndRentBeforeOrderByStartRentDesc(userId,
                        LocalDateTime.now(), pageable));
            case "FUTURE":
                return bookingToDto(repository.findAllByItemOwnerIdAndStartRentAfterOrderByStartRentDesc(userId,
                        LocalDateTime.now(), pageable));
            case "WAITING":
                return bookingToDto(repository.findAllByItemOwnerIdAndStatusOrderByStartRent(userId,
                        Status.WAITING, pageable));
            case "REJECTED":
                return bookingToDto(repository.findAllByItemOwnerIdAndStatusOrderByStartRent(userId,
                        Status.REJECTED, pageable));
        }
        throw new UnsupportedStatus("Unknown state: " + status);
    }

    @Override
    public List<BookingDto> getAllBookingByStatusFromBooker(Long userId, String status, Pageable pageable) {
        getUserById(userId);
        switch (status) {
            case "ALL":
                return bookingToDto(repository.findAllByBookerIdOrderByStartRentDesc(userId, pageable));
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return bookingToDto(repository.findAllByBookerIdAndStartRentBeforeAndEndRentAfterOrderByStartRentDesc(
                        userId, now, now, pageable));
            case "PAST":
                return bookingToDto(repository.findAllByBookerIdAndEndRentBeforeOrderByStartRentDesc(userId,
                        LocalDateTime.now(), pageable));
            case "FUTURE":
                return bookingToDto(repository.findAllByBookerIdAndStartRentAfterOrderByStartRentDesc(userId,
                        LocalDateTime.now(), pageable));
            case "WAITING":
                return bookingToDto(repository.findAllByBookerIdAndStatusOrderByStartRent(userId, Status.WAITING, pageable));
            case "REJECTED":
                return bookingToDto(repository.findAllByBookerIdAndStatusOrderByStartRent(userId, Status.REJECTED, pageable));
        }
        throw new UnsupportedStatus("Unknown state: " + status);
    }

    @Override
    public BookingDto getBookingByIdAndUserId(Long bookingId, Long userId) {
        getUserById(userId);
        final Booking booking = getBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new NotFoundException("Booking for you not found");
        }
        return bookingToDto(booking);
    }

    private Booking getBookingById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException("Booking not found! booking id: " + id));
    }

    private Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Item not found! Item id: " + id));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NotFoundException("User id: '" + id + "' not found, please check user id"));
    }
}
