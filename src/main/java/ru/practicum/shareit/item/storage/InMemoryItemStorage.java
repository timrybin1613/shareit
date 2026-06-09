package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 0;

    @Override
    public Item save(Item item) {
        long id = getNextId();
        item.setId(id);
        items.put(id, item);
        return item;
    }

    @Override
    public Optional<Item> findById(long id) {
        return items.values().stream().filter(item -> item.getId() == id).findFirst();
    }

    @Override
    public List<Item> findAvailableItemsByText(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter((item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findAllForUser(Long userId) {
        return items.values().stream()
                .filter(item -> Objects.equals(item.getOwner().getId(), userId)).collect(Collectors.toList());
    }

    private long getNextId() {
        return nextId++;
    }
}
