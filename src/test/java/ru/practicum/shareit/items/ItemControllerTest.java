package ru.practicum.shareit.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserDto;

import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.practicum.shareit.user.UserMapper.dtoToUser;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    private final UserDto user1 = new UserDto(1L, "user1", "user1@mail.com");
    private final UserDto user2 = new UserDto(2L, "user2", "user2@mail.com");

    private final ItemInputDto item1 = new ItemInputDto("item1", "descr1", true, null);
    private final ItemInputDto item2 = new ItemInputDto("item2", "desc2", true, null);

    private final ItemInputDto item3 = new ItemInputDto("item3", "desc3", true, null);

    private final ItemDto itemDto1 = new ItemDto(1L, "item1", "descr1", true,
            dtoToUser(user1), null, null, new ArrayList<>(), null);

    private final ItemDto itemDto2 = new ItemDto(2L, "item2", "descr2", true,
            dtoToUser(user1), null, null, new ArrayList<>(), null);

    private final ItemDto itemDto3 = new ItemDto(3L, "item3", "descr3", true,
            dtoToUser(user2), null, null, new ArrayList<>(), null);

    @MockBean
    private ItemService service;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void createNewItem_returnDto() throws Exception {
        when(service.createNewItem(any(ItemInputDto.class), anyLong())).thenReturn(itemDto1);

        mockMvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", user1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())));

        verify(service, times(1)).createNewItem(any(ItemInputDto.class), anyLong());
    }
}