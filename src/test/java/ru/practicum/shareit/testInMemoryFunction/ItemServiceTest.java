package ru.practicum.shareit.testInMemoryFunction;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author MR.k0F31n
 */
@SpringBootTest
public class ItemServiceTest {
    private final ItemService itemService;

    @Autowired
    public ItemServiceTest(UserService userService, ItemService itemService) {
        this.itemService = itemService;
    }

    @Test
    void createItems_expectedCorrect() {
        Item item1 = new Item(null, "дрель", "Дрель - лучший помошник соседа в воскресенье",
                true, null, false);
        ItemDto itemDto1 = itemService.createNewItem(item1, 1L);

        Item item3 = new Item(null, "ШпаТель", "Чтоб по стенке 'шик-шик'",
                true, null, false);
        ItemDto itemDto3 = itemService.createNewItem(item3, 2L);

        Item item4 = new Item(null, "Что-то Что будет удалено", "Удалить!!!",
                true, null, false);
        ItemDto itemDto4 = itemService.createNewItem(item4, 2L);

        assertEquals(2, itemService.getAllItemsByOwner(1L).size(), "Чего-то не хватает");
        assertEquals(3, itemService.getAllItemsByOwner(2L).size(), "Чего-то не хватает");
    }

    @Test
    void createItemsNotOwner_expectedThrows() {
        Item itemNotOwner = new Item(null, "Плебей", "Без племени",
                true, null, false);

        Throwable throwableNotFound = assertThrows(NotFoundException.class,
                () -> itemService.createNewItem(itemNotOwner, 999L));
        assertEquals("User not found! User id: " + 999L,
                throwableNotFound.getMessage(), "Мы создали чудовище!");
    }

    @Test
    void createItemsNotAvailable_expectedThrows() {
        Item itemNotOwner = new Item(null, "Плебей", "Без племени",
                null, null, false);

        Throwable throwableNotFound = assertThrows(ValidationException.class,
                () -> itemService.createNewItem(itemNotOwner, 1L));
        assertEquals("Available null",
                throwableNotFound.getMessage(), "Мы создали чудовище без права на аренду!");
    }

    @Test
    void getById_ExpectedCorrect() {
        ItemDto itemDto = itemService.getItemById(1L);

        assertEquals("Соседка", itemDto.getName(), "Что-то не так с именем");
        assertEquals("выбирайте соседей правильно", itemDto.getDescription(),
                "Что-то не так с описанием");
        assertEquals(2L, itemDto.getOwnerId(), "Не тот владелец");
        assertEquals(true, itemDto.getAvailable(), "Что-то не так с арендой");
    }

    @Test
    void getById_ExpectedThrows() {
        Throwable throwableNotFound = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(999L));
        assertEquals("Item not found! Item id: " + 999L,
                throwableNotFound.getMessage(), "На дне кто-то есть");
    }

    @Test
    void tryDeleteItemNotOwner_ExpectedThrows() {
        Throwable throwableNotOwner = assertThrows(NotFoundException.class,
                () -> itemService.deleteItem(4L, 1L));
        assertEquals("Unable to update item , user does not have such item",
                throwableNotOwner.getMessage(), "Удалять должны только владельцы!");
    }

    @Test
    void tryDeleteItem_ExpectedCorrect() {
        itemService.deleteItem(4L, 2L);
        Throwable throwableNotFound = assertThrows(NotFoundException.class,
                () -> itemService.getItemById(4L));
        assertEquals("Item not found! Item id: " + 4L,
                throwableNotFound.getMessage(), "Предмет не удалился");
    }

    @Test
    void updateItem_TestAllExceptions() {
        Item itemForUpdate = new Item(null, "Самый кривой обьект", "Точно такое же описание",
                true, null, false);
        Throwable throwableNotOwner = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemForUpdate, 2L, 2L));
        assertEquals("Unable to update item , user does not have such item",
                throwableNotOwner.getMessage(), "Обновлять должны только владельцы!");

        Throwable throwableNotFound = assertThrows(NotFoundException.class,
                () -> itemService.updateItem(itemForUpdate, 999L, 2L));
        assertEquals("Item not found! Item id: " + 999L,
                throwableNotFound.getMessage(), "Нашли владельца пропажи, а не должны!");
    }

    @Test
    void updateItem_Correct() {
        Item item2 = new Item(null, "Сосед", "Собственно собственник дрели",
                true, null, false);
        ItemDto itemDto2 = itemService.createNewItem(item2, 1L);

        Item itemForUpdate = new Item(null, "Соседка", "выбирайте соседей правильно",
                null, null, null);
        ItemDto itemDto = itemService.updateItem(itemForUpdate, 1L, 2L);
        assertEquals("Соседка", itemDto.getName());
    }

    @Test
    void search() {
        Item itemForSearch = new Item(null, "СаМЫй кРИВой обьект", "Точно такое же описание",
                true, null, false);
        ItemDto itemDto = itemService.createNewItem(itemForSearch, 2L);
        Long idExpected = itemDto.getId();

        String requestSearch = "КриВОЙ";
        List<ItemDto> itemFound = itemService.searchItem(2L, requestSearch);
        assertEquals(idExpected, itemFound.get(0).getId(), "Мы взяли ложный след");
        assertEquals("СаМЫй кРИВой обьект", itemFound.get(0).getName(), "Мы взяли ложный след");
    }
}
