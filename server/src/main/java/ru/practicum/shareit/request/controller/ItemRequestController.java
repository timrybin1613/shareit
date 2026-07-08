package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.util.HeaderConstants;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody ItemRequestCreateDto dto,
                                 @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        ItemRequestDto itemRequestDto = itemRequestService.save(dto, userId);
        log.info("ItemRequestId - {} ", itemRequestDto.getId());
        return itemRequestDto;
    }

    @GetMapping
    public List<ItemRequestDto> getByUserId(@RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemRequestService.getByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return itemRequestService.getAllForUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@PathVariable("requestId") Long requestId) {
        log.info("RequestId - {} ", requestId);
        return itemRequestService.getById(requestId);
    }
}
