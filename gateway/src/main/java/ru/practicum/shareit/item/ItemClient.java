package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> getItem(long userId, long id) {
        return get("/" + id, userId);
    }

    public ResponseEntity<Object> createItem(long userId, Object item) {
        return post("", userId, item);
    }

    public ResponseEntity<Object> updateItem(long id, long userId, Object item) {
        return patch("/" + id, userId, item);
    }

    public ResponseEntity<Object> searchItem(String text) {
        Map<String, java.lang.Object> parameters = Map.of(
                "text", text);
        return get("/search", null, parameters);
    }

    public ResponseEntity<Object> commentItem(long itemId, Object comment, long userId) {
        return post("/" + itemId + "/comment", userId, comment);
    }

}
