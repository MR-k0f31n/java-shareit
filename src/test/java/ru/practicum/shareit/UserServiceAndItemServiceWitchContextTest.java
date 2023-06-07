package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceAndItemServiceWitchContextTest {
/*

    @Test
    void updateUser_inputNull_returnNull() {
        UserDto updateUser = user1;
        updateUser.setId(1L);
        updateUser.setName(null);
        updateUser.setEmail(null);

        assertNull(service.updateUser(updateUser, 1L), "Обновились в null!");
    }

    @Test
    void updateItem_AllUpdate_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setName("ОтВерТка апргрейд");

        ItemDto itemBeforeUpdateName = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateName.getName());

        item1.setDescription("Убивает на растоянии");

        ItemDto itemBeforeUpdateDesc = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateDesc.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateDesc.getDescription());

        item1.setAvailable(false);

        ItemDto itemBeforeUpdateAvl = items.updateItem(item1, item1.getId(), user1.getId());

        assertEquals(item1.getName(), itemBeforeUpdateAvl.getName());
        assertEquals(item1.getDescription(), itemBeforeUpdateAvl.getDescription());
        assertEquals(item1.getAvailable(), itemBeforeUpdateAvl.getAvailable());
    }

    @Test
    void updateItem_userNotOwner_expectedError() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        item1.setName("ОтВерТка апргрейд");

        assertThrows(NotFoundException.class, () -> items.updateItem(item1, item1.getId(), 2L));
    }

    @Test
    void getItem_itemNotFound_userExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 1L));
    }

    @Test
    void getItem_itemNotFound_userNotExist() {
        assertThrows(NotFoundException.class, () -> items.getItemDtoById(99L, 2L));
    }

    @Test
    void getItem_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        ItemDto findItem = items.getItemDtoById(item1.getId(), user1.getId());
        assertEquals(item1.getName(), findItem.getName());
        assertEquals(item1.getDescription(), findItem.getDescription());
        items.deleteItem(item1.getId(), user1.getId());
    }

    @Test
    void getItemNotOwner_returnDto() {
        UserDto user = service.createNewUser(user1);
        UserDto user2 = new UserDto(2L, "name2", "emai2@mail.com");
        service.createNewUser(user2);

        ItemDto item1 = items.createNewItem(inputItem, user.getId());
        ItemDto findItem = items.getItemDtoById(item1.getId(), user2.getId());

        assertEquals(item1.getName(), findItem.getName());
        assertEquals(item1.getDescription(), findItem.getDescription());

        items.deleteItem(item1.getId(), user.getId());
        service.deleteUserById(user2.getId());
    }

    @Test
    void searchItem_returnDto() {
        service.createNewUser(user1);
        ItemDto item1 = items.createNewItem(inputItem, user1.getId());
        List<ItemDto> findItem = items.searchItem("ОтВЕРТка", 0, 10);
        assertEquals(item1.getName(), findItem.get(0).getName());
        assertEquals(item1.getDescription(), findItem.get(0).getDescription());
        items.deleteItem(item1.getId(), user1.getId());
    }

    @Test
    void addComment_textIsEmpty() {
        CommentInputDto input = new CommentInputDto();
        input.setText(" ");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }

    @Test
    void addComment_userNotRentItem() {
        CommentInputDto input = new CommentInputDto();
        input.setText("User not rent Item");
        assertThrows(ValidatorException.class, () -> items.addComment(1L, 1L, input));
    }

    @Test
    void deleteItemItem() {
        ItemDto itemFromDelete = items.createNewItem(new ItemInputDto("ItemFromDelete", "from delete desc", true, null), 1L);
        items.deleteItem(itemFromDelete.getId(), 1L);
    }

    @Test
    void deleteItemItem_userNotOwner_expectedError() {
        assertThrows(NotFoundException.class, () -> items.deleteItem(1L, 999L));
    }*/
}
