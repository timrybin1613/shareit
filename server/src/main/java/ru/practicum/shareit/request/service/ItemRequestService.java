package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto save(ItemRequestCreateDto dto, Long requesterId);

    List<ItemRequestDto> getByUserId(Long userId);

    List<ItemRequestDto> getAllForUser(Long userId);

    ItemRequestDto getById(Long requestId);
}
