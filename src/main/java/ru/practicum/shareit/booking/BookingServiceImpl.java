package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static ru.practicum.shareit.booking.BookingMapper.*;

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
    public BookingDto createNewBooking(BookingInputDTO bookingInputDTODto, Long userId) {
        Long itemId = bookingInputDTODto.getItemId();
        if (bookingInputDTODto.getStart().isAfter(bookingInputDTODto.getEnd()) ||
                bookingInputDTODto.getEnd().isEqual(bookingInputDTODto.getStart())) {
            throw new ValidatorException("Booking start time before or equals booking end time");
        }
        final Item item = getItemById(itemId);
        if (userId == item.getOwner().getId().longValue()) {
            throw new NotFoundException("you don't can rent own items");
        }
        final User booker = getUserById(userId);
        if (!item.getAvailable()) {
            throw new ValidatorException("Item not available");
        }
        final Booking booking = toBooking(bookingInputDTODto);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(Status.WAITING);
        return toBookingDto(repository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto setStatusBooking(Long bookingId, Long userId, Boolean isApproved) {
        final Booking booking = getBookingById(bookingId);
        if (!booking.getStatus().equals(Status.WAITING)) {
            throw new ValidatorException("Booking id '{ " + bookingId + " }' status not waiting");
        }
        final Item item = booking.getItem();
        final User owner = item.getOwner();
        if (!(owner.getId().equals(userId))) {
            throw new NotFoundException("Not owner reader");
        }
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        final BookingDto bookingDtoAfterUpdate = toBookingDto(repository.save(booking));
        log.debug("Booking change status. Booking info after update: " + bookingDtoAfterUpdate);
        return bookingDtoAfterUpdate;
    }

    @Override
    public List<BookingDto> getAllBookingByStatusFromOwner(Long userId, String status) {
        getUserById(userId);
        switch (status) {
            case "ALL":
                return toBookingDtoList(repository.findAllByItemOwnerIdOrderByStartRentDesc(userId));
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return toBookingDtoList(repository.findAllByItemOwnerIdAndStartRentBeforeAndEndRentAfterOrderByStartRent(
                        userId, now, now));
            case "PAST":
                return toBookingDtoList(repository.findAllByItemOwnerIdAndEndRentBeforeOrderByStartRentDesc(userId,
                        LocalDateTime.now()));
            case "FUTURE":
                return toBookingDtoList(repository.findAllByItemOwnerIdAndStartRentAfterOrderByStartRentDesc(userId,
                        LocalDateTime.now()));
            case "WAITING":
                return toBookingDtoList(repository.findAllByItemOwnerIdAndStatusOrderByStartRent(userId,
                        Status.WAITING));
            case "REJECTED":
                return toBookingDtoList(repository.findAllByItemOwnerIdAndStatusOrderByStartRent(userId,
                        Status.REJECTED));
        }
        throw new UnsupportedStatus("Unknown state: " + status);
    }

    @Override
    public List<BookingDto> getAllBookingByStatusFromBooker(Long userId, String status) {
        getUserById(userId);
        switch (status) {
            case "ALL":
                return toBookingDtoList(repository.findAllByBookerIdOrderByStartRentDesc(userId));
            case "CURRENT":
                LocalDateTime now = LocalDateTime.now();
                return toBookingDtoList(repository.findAllByBookerIdAndStartRentBeforeAndEndRentAfterOrderByStartRentDesc(
                        userId, now, now));
            case "PAST":
                return toBookingDtoList(repository.findAllByBookerIdAndEndRentBeforeOrderByStartRentDesc(userId,
                        LocalDateTime.now()));
            case "FUTURE":
                return toBookingDtoList(repository.findAllByBookerIdAndStartRentAfterOrderByStartRentDesc(userId,
                        LocalDateTime.now()));
            case "WAITING":
                return toBookingDtoList(repository.findAllByBookerIdAndStatusOrderByStartRent(userId, Status.WAITING));
            case "REJECTED":
                return toBookingDtoList(repository.findAllByBookerIdAndStatusOrderByStartRent(userId, Status.REJECTED));
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
        return toBookingDto(booking);
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