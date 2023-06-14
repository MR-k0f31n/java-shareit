package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public ResponseEntity<Object> getAllItemsCollectionByOwner(Integer from, Integer size, long ownerId) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={from}&size={size}", ownerId, parameters);
    }

    public ResponseEntity<Object> getItemById(long id, long userId) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> createNewItem(long ownerId, ItemRequestDto itemRequestDto) {
        return post("", ownerId, itemRequestDto);
    }

    public ResponseEntity<Object> updateItem(ItemRequestDto itemRequestDto, long itemId, long ownerId) {
        return patch("/" + itemId, ownerId, itemRequestDto);
    }

    public ResponseEntity<Object> deleteItem(long ownerId, long itemId) {
        return patch("/" + itemId, ownerId);
    }

    public ResponseEntity<Object> searchItem(Integer from, Integer size, String text) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search?text={text}&from={from}&size={size}", null, parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, long userId, CommentRequestDto requestDto) {
        return post("/" + itemId + "/comment", userId, requestDto);
    }
}
