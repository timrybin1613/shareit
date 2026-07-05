package ru.practicum.shareit.request.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class ItemRequestMapper {

    public ItemRequestDto toItemRequestDto(ItemRequest itemRequest, List<ItemShortDto> items) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public ItemRequest toItemRequest(ItemRequestCreateDto itemCreateDto,
                                     User requester,
                                     LocalDateTime created) {
        return ItemRequest.builder()
                .description(itemCreateDto.getDescription())
                .requester(requester)
                .created(created)
                .build();
    }

    public List<ItemRequestDto> toItemRequestDtos(List<ItemRequest> itemRequests,
                                                  Map<Long, List<ItemShortDto>> itemDtosByRequestId) {

        return itemRequests.stream().map(itemRequest -> this.toItemRequestDto(
                itemRequest, itemDtosByRequestId.getOrDefault(itemRequest.getId(), new ArrayList<>()))).toList();
    }

}
