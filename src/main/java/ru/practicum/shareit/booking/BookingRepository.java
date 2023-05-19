package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author MR.k0F31n
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    //List<Booking> findAllByBookerIdAndStatusOrderByStartDateTimeDesc(Long userId, String status);
}
