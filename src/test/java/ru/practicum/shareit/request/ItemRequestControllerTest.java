package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.ValidatorException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author MR.k0F31n
 */
@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Need item", null,
            LocalDateTime.now());
    private final ItemRequestInputDto input = new ItemRequestInputDto("Need item");
    private final ItemRequestWithAnswerDto requestWithAnswerDto = new ItemRequestWithAnswerDto(1L,
            "Need item", LocalDateTime.now(), new ArrayList<>());
    @MockBean
    private ItemRequestService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void createNewRequest_returnDto() throws Exception {
        when(service.createNewItemRequest(any(ItemRequestInputDto.class), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(input))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

        verify(service, times(1)).createNewItemRequest(any(ItemRequestInputDto.class), anyLong());
    }

    @Test
    void createNewRequest_requestIsNull_expectedStatus400() throws Exception {
        when(service.createNewItemRequest(any(ItemRequestInputDto.class), anyLong())).thenThrow(ValidatorException.class);

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(new ItemRequestInputDto()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).createNewItemRequest(any(ItemRequestInputDto.class), anyLong());
    }

    @Test
    void createNewRequest_requestIsBlank_expectedStatus400() throws Exception {
        when(service.createNewItemRequest(any(ItemRequestInputDto.class), anyLong())).thenThrow(ValidatorException.class);

        ItemRequestInputDto inputDto = new ItemRequestInputDto(" ");

        mockMvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(inputDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());

        verify(service, times(0)).createNewItemRequest(any(ItemRequestInputDto.class), anyLong());
    }

    @Test
    void getById_returnRequestDto() throws Exception {
        when(service.getAllItemRequestFromOtherUsers(anyLong(), anyLong())).thenReturn(requestWithAnswerDto);

        mockMvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(requestWithAnswerDto.getDescription())));

        verify(service, times(1)).getAllItemRequestFromOtherUsers(anyLong(), anyLong());
    }

    @Test
    void getAllItemRequestByUserIdWithAnswer_returnListDto_length1() throws Exception {
        when(service.getAllItemRequestByUserIdWithAnswer(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestWithAnswerDto));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(requestWithAnswerDto.getDescription())));

        verify(service, times(1)).getAllItemRequestByUserIdWithAnswer(anyLong(), anyInt(), anyInt());
    }

    @Test
    void getAllRequest_returnListDto_length1() throws Exception {
        when(service.getAllRequest(anyLong(), anyInt(), anyInt())).thenReturn(List.of(requestWithAnswerDto));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is(requestWithAnswerDto.getDescription())));

        verify(service, times(1)).getAllRequest(anyLong(), anyInt(), anyInt());
    }
}