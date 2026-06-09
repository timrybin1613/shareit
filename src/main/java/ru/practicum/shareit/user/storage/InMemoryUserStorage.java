package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private static Map<Long, User> users = new HashMap<>();
    private long nextId = 0;

    @Override
    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(users.get(id));
    }


    @Override
    public Optional<User> getUserByEmail(String email) {
        return users.values().stream().filter(user -> user.getEmail().equals(email)).findFirst();
    }

    @Override
    public Optional<User> addUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> updateUser(User user) {
        return Optional.ofNullable(users.put(user.getId(), user));
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    private long getNextId() {
        return nextId++;
    }
}
