package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;

import java.time.LocalDateTime;

/**
 * @author MR.k0F31n
 */
@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {
    ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Need item", null, LocalDateTime.now());
    @MockBean
    private ItemService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

}
