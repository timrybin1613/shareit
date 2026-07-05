package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemShortDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final ItemRequestMapper itemRequestMapper;

    @Override
    @Transactional
    public ItemRequestDto save(ItemRequestCreateDto dto, Long requesterId) {
        log.debug("save ItemRequestDto");
        UserDto user = userService.findById(requesterId);
        LocalDateTime now = LocalDateTime.now();
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(dto, userMapper.toUser(user, requesterId), now);
        return itemRequestMapper.toItemRequestDto(itemRequestStorage.save(itemRequest), new ArrayList<>());
    }

    @Override
    public List<ItemRequestDto> getByUserId(Long userId) {
        userService.findById(userId);

        return mapRequests(
                itemRequestStorage.findByRequester_IdOrderByCreatedDesc(userId)
        );
    }

    @Override
    public List<ItemRequestDto> getAllForUser(Long userId) {
        userService.findById(userId);

        return mapRequests(
                itemRequestStorage.findByRequester_IdIsNotOrderByCreatedDesc(userId)
        );
    }

    public ItemRequestDto getById(Long requestId) {
        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));
        List<Item> items = itemStorage.findByRequest_IdIn(List.of(itemRequest.getId()));

        if (items.isEmpty()) {
            return itemRequestMapper.toItemRequestDto(itemRequest, new ArrayList<>());
        }
        List<ItemShortDto> itemShortDtos = itemMapper.toItemShortDtos(items);
        return itemRequestMapper.toItemRequestDto(itemRequest, itemShortDtos);
    }

    private List<ItemRequestDto> mapRequests(List<ItemRequest> requests) {
        if (requests.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> requestIds = requests.stream()
                .map(ItemRequest::getId)
                .toList();

        List<Item> items = itemStorage.findByRequest_IdIn(requestIds);

        if (items.isEmpty()) {
            return itemRequestMapper.toItemRequestDtos(requests, Collections.emptyMap());
        }

        Map<Long, List<ItemShortDto>> itemDtosByRequestId = items.stream()
                .collect(Collectors.groupingBy(
                        item -> item.getRequest().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                itemMapper::toItemShortDtos
                        )
                ));

        return itemRequestMapper.toItemRequestDtos(requests, itemDtosByRequestId);
    }
}
