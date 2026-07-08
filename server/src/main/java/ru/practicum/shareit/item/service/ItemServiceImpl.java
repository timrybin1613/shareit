package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.storage.ItemBookingDateProjection;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final ItemMapper mapper;
    private final UserMapper userMapper;
    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;
    private final BookingStorage bookingStorage;
    private final BookingService bookingService;
    private final ItemRequestStorage itemRequestStorage;
    private final UserService userService;

    @Override
    public List<ItemDtoWithDetails> findAllForUser(Long userId) {
        log.debug("findAllForUser, userId={}", userId);
        validateUserById(userId);
        List<Item> items = storage.findByOwnerId(userId);

        Map<Long, List<Comment>> commentByItemId = loadCommentsByItems(items);

        List<Long> itemIds = items.stream().map(Item::getId).toList();
        LocalDateTime now = LocalDateTime.now();

        Map<Long, LocalDateTime> lastBookingsByItemIds = loadLastBookingsByItemIds(itemIds, now);
        Map<Long, LocalDateTime> nextBookingsByItemId = loadNextBookingsByItemIds(itemIds, now);

        return items.stream().map(item -> {
            return mapper.toItemDtoWithDetails(item,
                    lastBookingsByItemIds.getOrDefault(item.getId(), null),
                    nextBookingsByItemId.getOrDefault(item.getId(), null),
                    commentMapper.toCommentDtos(commentByItemId.getOrDefault(item.getId(), List.of())));
        }).toList();
    }

    private Map<Long, List<Comment>> loadCommentsByItems(List<Item> items) {
        List<Comment> comments = commentStorage.findByItemIdIn(
                items.stream().map(Item::getId).collect(Collectors.toList()));

        return comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
    }

    private Map<Long, LocalDateTime> loadLastBookingsByItemIds(List<Long> itemIds, LocalDateTime now) {
        List<ItemBookingDateProjection> lastBookingProjection = bookingStorage.getLastBookingByIdIn(itemIds, now);
        return lastBookingProjection.stream().collect(Collectors.toMap(
                ItemBookingDateProjection::getItemId,
                ItemBookingDateProjection::getDateBooking));
    }

    private Map<Long, LocalDateTime> loadNextBookingsByItemIds(List<Long> itemIds, LocalDateTime now) {
        List<ItemBookingDateProjection> nextBookingProjection = bookingStorage.getNextBookingByIdIn(itemIds, now);
        return nextBookingProjection.stream().collect(Collectors.toMap(
                ItemBookingDateProjection::getItemId,
                ItemBookingDateProjection::getDateBooking));
    }

    @Override
    public ItemDtoWithDetails findById(Long id, Long userId) {
        log.debug("ItemService.findById({})", id);
        Item item = getItemOrThrowNotFound(id);
        boolean isOwner = item.getOwner().getId().equals(userId);
        LocalDateTime lastBooking = null;
        LocalDateTime nextBooking = null;
        if (isOwner) {
            LocalDateTime now = LocalDateTime.now();
            lastBooking = bookingService.getLastBookingForItem(id, now);
            nextBooking = bookingService.getNextBookingForItem(id, now);
        }
        List<CommentDto> comments = commentMapper.toCommentDtos(commentStorage.findByItemId(id));
        return mapper.toItemDtoWithDetails(item, lastBooking, nextBooking, comments);

    }

    @Override
    @Transactional
    public ItemDto create(ItemCreateDto itemCreateDto, Long ownerId) {
        log.debug("create, itemCreateDto={}, ownerId={}", itemCreateDto, ownerId);
        UserDto userDto = userService.findById(ownerId);

        ItemRequest itemRequest = null;
        if (itemCreateDto.getRequestId() != null) {
            itemRequest = itemRequestStorage.findById(itemCreateDto.getRequestId()).orElseThrow(
                    () -> new NotFoundException("request not found")
            );
        }

        User user = userMapper.toUser(userDto, ownerId);
        Item item = mapper.toItem(itemCreateDto, user, itemRequest);
        return mapper.toItemDto(storage.save(item));
    }

    @Override
    @Transactional
    public ItemDto update(Long itemId, ItemUpdateDto dto, Long ownerId) {
        log.debug("update, itemId={}, ownerId={}", itemId, ownerId);
        UserDto userRequestDto = userService.findById(ownerId);
        Item itemToUpdate = getItemOrThrowNotFound(itemId);
        if (!Objects.equals(itemToUpdate.getOwner().getId(), userRequestDto.getId())) {
            throw new NotFoundException("Item not found");
        }

        if (dto.getName() != null) {
            itemToUpdate.setName(dto.getName());
        }

        if (dto.getDescription() != null) {
            itemToUpdate.setDescription(dto.getDescription());
        }

        if (dto.getAvailable() != null) {
            itemToUpdate.setAvailable(dto.getAvailable());
        }
        return mapper.toItemDto(itemToUpdate);
    }

    @Override
    public List<ItemDto> findAvailableItemsByText(String text) {
        log.debug("findAvailableItemsByText, text={}", text);
        if (text.isBlank()) {
            log.debug("text for search is blank");
            return List.of();
        }

        return storage.findAvailableItemsByText(text)
                .stream().map(mapper::toItemDto).collect(Collectors.toList());
    }

    private void validateUserById(Long userId) {
        log.debug("validateUserById, userId={}", userId);
        if (userService.findById(userId) == null) {
            throw new NotFoundException("User not found");
        }
    }

    private Item getItemOrThrowNotFound(Long itemId) {
        log.debug("getItemOrThrowNotFound, itemId={}", itemId);
        return storage.findById(itemId).orElseThrow(() -> {
            log.debug("item not found with id {}", itemId);
            return new NotFoundException("item not found with id " + itemId);
        });
    }

}
