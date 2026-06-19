package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<List<ItemDtoWithDetails>> findAllForUser(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDtoWithDetails> items = itemService.findAllForUser(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDtoWithDetails> getItemBy(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long id) {
        ItemDtoWithDetails itemDto = itemService.findById(id, userId);
        return ResponseEntity.ok().body(itemDto);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @Valid
            @RequestBody ItemCreateDto itemCreateDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto createdItem = itemService.create(itemCreateDto, userId);
        return ResponseEntity.ok().body(createdItem);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemDto> updateItem(
            @PathVariable Long id,
            @RequestBody ItemUpdateDto itemUpdateDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto updateItem = itemService.update(id, itemUpdateDto, userId);
        return ResponseEntity.ok().body(updateItem);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItem(
            @RequestParam String text) {
        List<ItemDto> items = itemService.findAvailableItemsByText(text);
        return ResponseEntity.ok().body(items);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<CommentDto> addComment(
            @PathVariable Long id,
            @RequestBody CommentCreateDto commentCreateDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        CommentDto commentDto = commentService.addComment(commentCreateDto, id, userId);
        return ResponseEntity.ok().body(commentDto);
    }
}
