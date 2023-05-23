package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MR.k0F31n
 */
public class BookingMapper {

    public static Booking dtoToBooking(BookingInputDto bookingInputDTO) {
        return new Booking(
                null,
                bookingInputDTO.getStart(),
                bookingInputDTO.getEnd(),
                new Item(),
                new User(),
                null
        );
    }

    public static BookingDto bookingToDto(Booking booking) {
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

    public static BookingShortDto bookingToShortDto(Booking booking) {
        return new BookingShortDto(
                booking.getId(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStartRent(),
                booking.getEndRent()
        );
    }

    public static List<BookingDto> bookingToDto(Iterable<Booking> bookings) {
        List<BookingDto> bookingDtoList = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingDtoList.add(bookingToDto(booking));
        }
        return bookingDtoList;
    }
}
