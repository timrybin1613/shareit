package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

@SpringBootTest
@Transactional
public class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserStorage userStorage;

    @Test
    void updateUser() {
        User user = userStorage.save(User.builder()
                .name("user")
                .email("e@ma.il")
                .build());

        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("m@el.ee");
        UserDto updatedUser = userService.updateUser(userUpdateDto, user.getId());

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedUser.getName(), user.getName());
        Assertions.assertEquals(updatedUser.getEmail(), userUpdateDto.getEmail());
    }

}
