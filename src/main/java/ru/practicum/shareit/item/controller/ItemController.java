package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> findAllForUser(
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemDto> items = itemService.findAllForUser(userId);
        return ResponseEntity.ok(items);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(
            @PathVariable Long id) {
        ItemDto itemDto = itemService.findById(id);
        return ResponseEntity.ok().body(itemDto);
    }

    @PostMapping
    public ResponseEntity<ItemDto> createItem(
            @Valid
            @RequestBody ItemDto itemDto,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto createdItem = itemService.create(itemDto, userId);
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
}
