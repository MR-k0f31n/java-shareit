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
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.user.UserMapper.dtoToUser;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {
    private final UserDto user1 = new UserDto(1L, "user1", "user1@mail.com");
    private final UserDto user2 = new UserDto(2L, "user2", "user2@mail.com");
    private final ItemInputDto item1 = new ItemInputDto("item1", "descr1", true, null);
    private final CommentDto commentDto1 = new CommentDto(1L, "text from comment1", 1L, user2.getName(),
            LocalDateTime.now());
    private final ItemDto itemDto2 = new ItemDto(2L, "item2", "descr2", true,
            dtoToUser(user1), null, null, List.of(commentDto1), null);
    private final ItemDto itemDto1 = new ItemDto(1L, "item1", "descr1", true,
            dtoToUser(user1), null, null, new ArrayList<>(), null);
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

    @Test
    void getItemById_returnItemDto() throws Exception {
        when(service.getItemDtoById(anyLong(), anyLong())).thenReturn(itemDto1);

        mockMvc.perform(get("/items/{id}", itemDto1.getId())
                        .header("X-Sharer-User-Id", itemDto1.getOwner().getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())));

        verify(service, times(1)).getItemDtoById(anyLong(), anyLong());
    }

    @Test
    void addComments_returnItemWitchComment() throws Exception {
        when(service.addComment(anyLong(), anyLong(), any())).thenReturn(commentDto1);
        CommentInputDto comment = new CommentInputDto();
        comment.setText("text from comment1");

        mockMvc.perform(post("/items/{itemId}/comment", itemDto2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment))
                        .param("itemId", String.valueOf(itemDto2.getId()))
                        .header("X-Sharer-User-Id", user2.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto1.getItemId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto1.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto1.getAuthorName())))
                .andExpect(jsonPath("$.itemId", is(commentDto1.getItemId()), Long.class));

        verify(service, times(1)).addComment(anyLong(), anyLong(), any());
    }

    @Test
    void updateItem_returnItemDto() throws Exception {
        ItemDto itemFromUpdate = itemDto1;
        itemFromUpdate.setName("Name Update");
        itemFromUpdate.setDescription("Update desc");
        itemFromUpdate.setAvailable(false);

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(itemFromUpdate);

        mockMvc.perform(patch("/items/{id}", itemDto1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemFromUpdate))
                        .header("X-Sharer-User-Id", user1.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemFromUpdate.getName())))
                .andExpect(jsonPath("$.description", is(itemFromUpdate.getDescription())))
                .andExpect(jsonPath("$.available", is(itemFromUpdate.getAvailable())));

        verify(service, times(1)).updateItem(any(ItemDto.class), anyLong(), anyLong());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/items/{id}", itemDto1.getId())
                        .header("X-Sharer-User-Id", user1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service, times(1))
                .deleteItem(anyLong(), anyLong());
    }
}