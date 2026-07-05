package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestCreateDto dto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequestDto itemRequestDto = itemRequestService.save(dto, userId);
        log.info("ItemRequestId - {} ", itemRequestDto.getId());
        return itemRequestDto;
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getAllForUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable("requestId") Long requestId) {
        log.info("RequestId - {} ", requestId);
        return itemRequestService.getById(requestId);
    }
}
