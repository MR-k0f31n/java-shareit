package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;
import java.util.List;

import static ru.practicum.shareit.booking.BookingMapper.toBooking;
import static ru.practicum.shareit.booking.BookingMapper.toBookingDto;

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

    @Override
    public BookingDto createNewBooking(BookingInputDTO bookingInputDTODto, Long userId) {
        long itemId = bookingInputDTODto.getItemId();
        if (bookingInputDTODto.getEnd().isBefore(bookingInputDTODto.getStart()) ||
                bookingInputDTODto.getEnd().equals(bookingInputDTODto.getStart())) {
            throw new ValidatorException("Booking start time before or equals booking end time");
        }
        final Item item = getItemById(itemId);
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

    @Override
    public BookingDto setStatusBooking(Long bookingId, Long userId, Boolean isApproved) {
        final Booking booking = getBookingById(bookingId);
        final Item item = booking.getItem();
        final User owner = item.getOwner();
        if (!booking.getStatus().equals(Status.WAITING) || !owner.getId().equals(userId) || !item.getAvailable()) {
            throw new ValidatorException("Booking id '{ " + bookingId + " }' can not update check status or owner");
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

        return null;
    }

    @Override
    public List<BookingDto> getAllBookingByStatusFromBooker(Long userId, String status) {
        return null;
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
