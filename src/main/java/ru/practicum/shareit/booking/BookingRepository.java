package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author MR.k0F31n
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartRentDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartRentAfterOrderByStartRentDesc(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndEndRentBeforeOrderByStartRentDesc(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartRentBeforeAndEndRentAfterOrderByStartRentDesc(Long bookerId,
                                                                                         LocalDateTime time,
                                                                                         LocalDateTime time2);

    List<Booking> findAllByBookerIdAndStatusOrderByStartRent(Long bookerId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartRentDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartRentBeforeAndEndRentAfterOrderByStartRent(Long ownerId,
                                                                                        LocalDateTime time,
                                                                                        LocalDateTime time2);

    List<Booking> findAllByItemOwnerIdAndEndRentBeforeOrderByStartRentDesc(Long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerIdAndStartRentAfterOrderByStartRentDesc(Long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartRent(Long ownerId, Status status);

    Optional<Booking> findFirstByItemIdAndStartRentBeforeAndStatusOrderByStartRentDesc(Long itemId,
                                                                                       LocalDateTime localDate,
                                                                                       Status status);

    Optional<Booking> findFirstByItemIdAndStartRentAfterAndStatusOrderByStartRentAsc(Long itemId,
                                                                                     LocalDateTime localDate,
                                                                                     Status status);

    Boolean existsByBookerIdAndItemIdAndEndRentBefore(Long bookerId, Long itemId, LocalDateTime dateTime);
}
