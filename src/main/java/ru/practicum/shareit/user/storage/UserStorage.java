package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserStorage {

    Optional<User> getUserById(Long id);

    Optional<User> getUserByEmail(String email);

    Optional<User> addUser(User user);

    Optional<User> updateUser(User user);

    void deleteUser(Long id);
}
