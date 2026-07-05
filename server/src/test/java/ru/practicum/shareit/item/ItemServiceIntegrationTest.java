package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemStorage itemStorage;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private ItemService itemService;

    @Test
    void createItem_shouldSaveItem() {

        User owner = userStorage.save(User.builder()
                .name("owner")
                .email("owner@mail.ru")
                .build());

        ItemCreateDto dto = ItemCreateDto.builder()
                .name("item")
                .available(true)
                .description("description")
                .build();

        ItemDto item = itemService.create(dto, owner.getId());

        assertNotNull(item.getId());
        assertEquals(true, item.getAvailable());
        assertEquals(dto.getName(), item.getName());
        assertEquals(dto.getDescription(), item.getDescription());

        Item itemFromDb = itemStorage.findById(item.getId()).orElseThrow();

        assertEquals(item.getName(), itemFromDb.getName());
        assertEquals(item.getDescription(), itemFromDb.getDescription());
    }

}
