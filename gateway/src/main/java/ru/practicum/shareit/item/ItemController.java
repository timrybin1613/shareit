package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.util.HeaderConstants;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> findAllForUser(
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemClient.getItems(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getItemBy(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @PathVariable Long id) {
        return itemClient.getItem(userId, id);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(
            @Valid
            @RequestBody ItemCreateDto itemCreateDto,
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemClient.createItem(userId, itemCreateDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateDto itemUpdateDto,
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemClient.updateItem(id, userId, itemUpdateDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(
            @RequestParam String text) {
        return itemClient.searchItem(text);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> addComment(
            @Valid
            @PathVariable Long id,
            @RequestBody CommentCreateDto commentCreateDto,
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemClient.commentItem(id, commentCreateDto, userId);
    }
}
