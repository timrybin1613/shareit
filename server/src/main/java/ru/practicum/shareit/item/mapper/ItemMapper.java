package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ItemMapper {

    public Item toItem(ItemCreateDto dto, User owner, ItemRequest itemRequest) {
        return Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .request(itemRequest)
                .available(dto.getAvailable())
                .owner(owner)
                .build();
    }

    public ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public ItemShortDto toItemShortDto(Item item) {
        return ItemShortDto.builder()
                .id(item.getId())
                .name(item.getName())
                .ownerId(item.getOwner().getId())
                .build();
    }

    public List<ItemShortDto> toItemShortDtos(List<Item> items) {
        return items.stream().map(this::toItemShortDto).toList();
    }

    public ItemDtoWithDetails toItemDtoWithDetails(Item item,
                                                   LocalDateTime lastBooking,
                                                   LocalDateTime nextBooking,
                                                   List<CommentDto> comments) {
        return ItemDtoWithDetails.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}
