package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithDetails;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {

    List<ItemDtoWithDetails> findAllForUser(Long userId);

    ItemDtoWithDetails findById(Long id, Long userId);

    ItemDto create(ItemCreateDto itemCreateDto, Long ownerId);

    ItemDto update(Long itemId, ItemUpdateDto dto, Long ownerId);

    List<ItemDto> findAvailableItemsByText(String text);
}
