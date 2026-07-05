package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {

    UserDto findById(long id);

    UserDto addUser(UserDto userDto);

    UserDto updateUser(UserUpdateDto userDto, Long id);

    void deleteUser(Long id);
}
