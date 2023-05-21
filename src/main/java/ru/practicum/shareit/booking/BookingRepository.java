package ru.practicum.shareit.booking;

import com.sun.source.tree.LambdaExpressionTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author MR.k0F31n
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartRentDesc(Long bookerId);

    List<Booking> findAllByBookerIdAndStartRentAfterAndStatusOrderByStartRent
            (Long bookerId, LocalDateTime time, Status status);

    List<Booking> findAllByBookerIdAndEndRentBeforeOrderByStartRent(Long bookerId, LocalDateTime time);

    List<Booking> findAllByBookerIdAndStartRentBeforeAndEndRentAfterAndStatusOrderByStartRent
            (Long bookerId, LocalDateTime time, LocalDateTime time2, Status status);

    List<Booking> findAllByBookerIdAndStatusOrderByStartRent(Long bookerId, Status status);

    List<Booking> findAllByItemOwnerIdOrderByStartRentDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdAndStartRentBeforeAndEndRentAfterOrderByStartRent
            (Long ownerId, LocalDateTime time, LocalDateTime time2);

    List<Booking> findAllByItemOwnerIdAndEndRentBeforeOrderByStartRent(Long ownerId, LocalDateTime time);

    List<Booking> findAllByItemOwnerIdAndStartRentAfterAndStatusOrderByStartRent
            (Long ownerId, LocalDateTime time, Status status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartRent(Long ownerId, Status status);

    Optional<Booking> findFirstByItemIdAndStartRentBeforeAndStatusOrderByEndRentDesc(Long itemId,
                                                                             LocalDateTime localDate,
                                                                             Status status);

    Optional<Booking> findFirstByItemIdAndStartRentAfterAndStatusOrderByEndRentAsc(Long itemId,
                                                                           LocalDateTime localDate,
                                                                           Status status);

    Boolean existsByBookerIdAndItemIdAndEndRentBefore(Long bookerId, Long itemId, LocalDateTime dateTime);
}
