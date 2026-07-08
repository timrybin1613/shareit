package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class RequestServiceIntegrationTest {

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private ItemRequestStorage itemRequestStorage;

    @Autowired
    private ItemRequestService itemRequestService;

    @Test
    void getAllForUser_shouldReturnRequestsOfOtherUsers() {
        User user = userStorage.save(User.builder()
                .email("user@mail.ru")
                .name("user")
                .build());

        User requester = userStorage.save(
                User.builder()
                        .email("requeset@mail.ru")
                        .name("requester")
                        .build());

        itemRequestStorage.save(
                ItemRequest.builder()
                        .created(LocalDateTime.now())
                        .requester(requester)
                        .description("description_check")
                        .build());

        itemRequestStorage.save(
                ItemRequest.builder()
                        .created(LocalDateTime.now())
                        .requester(user)
                        .description("description")
                        .build());

        List<ItemRequestDto> itemRequests = itemRequestService.getAllForUser(user.getId());

        Assertions.assertEquals(1, itemRequests.size());
        Assertions.assertEquals("description_check", itemRequests.getFirst().getDescription());
    }

}
