package ru.practicum.shareit.user.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserStorage storage;
    private final UserMapper mapper;

    @Override
    public UserDto findById(long id) {
        log.debug("findById, id={}", id);
        User user = storage.findById(id).orElseThrow(() -> {
            log.debug("User with id {} not found", id);
            return new NotFoundException("User with id " + id + " not found");
        });

        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto addUser(UserDto dto) {
        log.debug("addUser, dto={}", dto);
        validateEmailUniqueness(dto.getEmail());
        User user = mapper.toUser(dto, null);
        storage.save(user);
        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserUpdateDto dto, Long id) {
        log.debug("updateUser, dto={}", dto);
        validateId(id);

        User user = storage.findById(id).orElseThrow(
                () -> new NotFoundException("User with id " + id + " not found")
        );

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        String emailForUpdate = dto.getEmail();

        if (emailForUpdate != null) {

            if (!emailForUpdate.equals(user.getEmail())) {
                validateEmailUniqueness(emailForUpdate);
                user.setEmail(emailForUpdate);
            }
        }

        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.debug("deleteUser, id={}", id);
        validateId(id);
        storage.deleteById(id);
    }

    private void validateEmailUniqueness(String email) {
        if (storage.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
    }

    private void validateId(Long id) {
        if (id == null || id < 1) {
            throw new ValidationException("Id is null or negative");
        }
    }

}
