package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStart(),
                bookingDto.getEnd(),
                new Item(),
                new User(),
                bookingDto.getStatus()
        );
    }

    public static Booking toBooking(BookingInputDTO bookingInputDTO) {
        return new Booking(
                null,
                bookingInputDTO.getStart(),
                bookingInputDTO.getEnd(),
                new Item(),
                new User(),
                null
        );
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStartRent(),
                booking.getEndRent(),
                booking.getItem().getId(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }

    public static List<BookingDto> toBookingDtoList(Iterable<Booking> bookings) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for(Booking booking : bookings) {
            bookingDtoList.add(toBookingDto(booking));
        }
        return bookingDtoList;
    }
}
