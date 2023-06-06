package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;
import ru.practicum.shareit.exception.NotFoundException;


import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    @MockBean
    private final ItemRequestService service;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Need item", null,
            LocalDateTime.now());
    private final ItemRequestInputDto input = new ItemRequestInputDto("Need item");
    private final ItemRequestWithAnswerDto requestWithAnswerDto = new ItemRequestWithAnswerDto(1L,
            "Need item", LocalDateTime.now(), new ArrayList<>());

    @Test
    void createNewRequest_returnDto () {
        when(service.createNewItemRequest(any(ItemRequestInputDto.class), anyLong())).thenReturn(itemRequestDto);

        ItemRequestDto requestDto = service.createNewItemRequest(input, 1L);

        assertNotNull(requestDto);
        assertEquals(itemRequestDto.getId(), requestDto.getId());
        assertEquals(itemRequestDto.getDescription(), requestDto.getDescription());
    }
}
