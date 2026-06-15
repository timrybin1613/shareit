package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final ItemMapper mapper;
    private final UserMapper userMapper;
    private final UserService userService;

    @Override
    public List<ItemDto> findAllForUser(Long userId) {
        log.debug("findAllForUser, userId={}", userId);
        validateUserById(userId);
        return storage.findAllForUser(userId).stream()
                .map(mapper::toItemDto).toList();
    }

    @Override
    public ItemDto findById(Long id) {
        log.debug("ItemService.findById({})", id);
        Item item = getItemOrThrowNotFound(id);
        return mapper.toItemDto(item);
    }

    @Override
    public ItemDto create(ItemDto itemDto, Long ownerId) {
        log.debug("create, itemDto={}, ownerId={}", itemDto, ownerId);
        UserDto userDto = userService.findById(ownerId);

        User user = userMapper.toUser(userDto, ownerId);
        Item item = mapper.toItem(itemDto, user);
        return mapper.toItemDto(storage.save(item));
    }

    @Override
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
