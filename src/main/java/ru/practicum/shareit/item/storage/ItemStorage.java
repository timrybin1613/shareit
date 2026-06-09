package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item save(Item item);

    Optional<Item> findById(long id);

    List<Item> findAvailableItemsByText(String text);

    List<Item> findAllForUser(Long userId);

}
