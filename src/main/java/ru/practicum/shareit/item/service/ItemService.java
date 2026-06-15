package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> findAllForUser(Long userId);

    ItemDto findById(Long id);

    ItemDto create(ItemDto itemDto, Long ownerId);

    ItemDto update(Long itemId, ItemUpdateDto dto, Long ownerId);

    List<ItemDto> findAvailableItemsByText(String text);
}
