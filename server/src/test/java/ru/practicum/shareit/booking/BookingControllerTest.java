package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author MR.k0F31n
 */
@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {
    private final BookingInputDto inputDto = new BookingInputDto(LocalDateTime.now().plusSeconds(20),
            LocalDateTime.now().plusMinutes(2), 1L);
    private final BookingDto bookingDto = new BookingDto(1L, inputDto.getStart(), inputDto.getEnd(), inputDto.getItemId(),
            new Item(), new User(), Status.WAITING);
    @MockBean
    private BookingService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void createNewBooking_bookingCorrect_returnBookingDto() throws Exception {

        when(service.createNewBooking(any(BookingInputDto.class), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class));

        verify(service, times(1)).createNewBooking(any(BookingInputDto.class), anyLong());
    }

    @Test
    void setStatusBooking_statusApproved_returnDto() throws Exception {

        when(service.setStatusBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).setStatusBooking(anyLong(), anyLong(), anyBoolean());
    }

    @Test
    void getBookingById_returnBookingDto() throws Exception {
        when(service.getBookingByIdAndUserId(anyLong(), anyLong())).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1)).getBookingByIdAndUserId(anyLong(), anyLong());
    }
}
