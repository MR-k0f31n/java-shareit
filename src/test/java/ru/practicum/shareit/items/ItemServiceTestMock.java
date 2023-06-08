package ru.practicum.shareit.items;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidatorException;
import ru.practicum.shareit.item.ItemDto;
import ru.practicum.shareit.item.ItemInputDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentInputDto;
import ru.practicum.shareit.user.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static ru.practicum.shareit.user.UserMapper.dtoToUser;

/**
 * @author MR.k0F31n
 */

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTestMock {
    @MockBean
    private final ItemService service;

    private final UserDto user1 = new UserDto(1L, "user1", "user1@mail.com");
    private final UserDto user2 = new UserDto(2L, "user2", "user2@mail.com");
    private final ItemInputDto item1 = new ItemInputDto("item1", "descr1", true, null);
    private final CommentDto commentDto1 = new CommentDto(1L, "text from comment1", 1L, user2.getName(),
            LocalDateTime.now());
    private final ItemDto itemDto2 = new ItemDto(2L, "item2", "descr2", true,
            dtoToUser(user1), null, null, List.of(commentDto1), null);
    private final ItemDto itemDto1 = new ItemDto(1L, "item1", "descr1", true,
            dtoToUser(user1), null, null, new ArrayList<>(), null);

    @Test
    void testGetAllItemsByOwnerId_returnListItemDto_length2() {
        when(service.getAllItemsByOwner(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemDto1, itemDto2));

        List<ItemDto> items = service.getAllItemsByOwner(user1.getId(), 0, 10);

        assertNotNull(items, "Предметов нет");
        assertEquals(2, items.size(), "Количесвто предметов не совпадает");
    }

    @Test
    void testCreateNewUser_returnItemDto() {
        when(service.createNewItem(any(ItemInputDto.class), anyLong())).thenReturn(itemDto1);

        ItemDto item = service.createNewItem(item1, user1.getId());

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(itemDto1.getName()));
        assertThat(item.getDescription(), equalTo(itemDto1.getDescription()));
    }

    @Test
    void updateItem_correctUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(false);
        updateItem.setDescription("Update");
        updateItem.setName("Update");

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(updateItem);

        ItemDto itemBeforeUpdate = service.updateItem(updateItem, itemDto1.getId(), user1.getId());

        assertEquals(updateItem.getName(), itemBeforeUpdate.getName(), "Name not update");
        assertEquals(updateItem.getAvailable(), itemBeforeUpdate.getAvailable(), "Available not update");
        assertEquals(updateItem.getDescription(), itemBeforeUpdate.getDescription(), "Description not update");
    }

    @Test
    void updateItem_allNull_expectedNull() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(null);
        updateItem.setDescription(null);
        updateItem.setName(null);

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(null);

        ItemDto itemBeforeUpdate = service.updateItem(updateItem, itemDto1.getId(), user1.getId());

        assertNull(itemBeforeUpdate);
    }

    @Test
    void updateItem_availableIsNull_availableNotUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setAvailable(null);

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(itemDto1);

        ItemDto itemBeforeUpdate = service.updateItem(updateItem, itemDto1.getId(), user1.getId());

        assertEquals(itemDto1.getAvailable(), itemBeforeUpdate.getAvailable());
    }

    @Test
    void updateItem_descriptionIsNull_descriptionNotUpdate() {
        ItemDto updateItem = itemDto1;
        updateItem.setDescription(null);

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenReturn(itemDto1);

        ItemDto itemBeforeUpdate = service.updateItem(updateItem, itemDto1.getId(), user1.getId());

        assertEquals(itemDto1.getDescription(), itemBeforeUpdate.getDescription());
    }

    @Test
    void updateItem_userNotOwner_expectedError() {
        ItemDto updateItem = itemDto1;
        updateItem.setName("Name update");

        when(service.updateItem(any(ItemDto.class), anyLong(), anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.updateItem(updateItem, itemDto1.getId(), user2.getId()));
    }

    @Test
    void getItemById_returnItemDto() {
        when(service.getItemDtoById(anyLong(), anyLong())).thenReturn(itemDto1);

        ItemDto findItem = service.getItemDtoById(1L, 1L);

        assertEquals(itemDto1.getName(), findItem.getName());
    }

    @Test
    void getItemByID_itemNotFound_expectedError() {
        when(service.getItemDtoById(anyLong(), anyLong())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> service.getItemDtoById(99L, 1L));
    }

    @Test
    void searchItem_returnListItemDto_length1() {
        when(service.searchItem(anyString(), anyInt(), anyInt())).thenReturn(List.of(itemDto1));

        assertEquals(1, service.searchItem("iTem1", 0, 10).size());
    }

    @Test
    void addComment_returnCommentDto() {
        when(service.addComment(anyLong(), anyLong(), any(CommentInputDto.class))).thenReturn(commentDto1);
        CommentInputDto comment = new CommentInputDto();
        comment.setText("text from comment1");

        CommentDto commentDto = service.addComment(2L, 1L, comment);

        assertEquals(commentDto1.getText(), commentDto.getText());
    }

    @Test
    void addComment_commentIsNull_expectedError() {
        when(service.addComment(anyLong(), anyLong(), any(CommentInputDto.class))).thenThrow(ValidatorException.class);

        assertThrows(ValidatorException.class, () -> service.addComment(2L, 1L, new CommentInputDto()));
    }
}
