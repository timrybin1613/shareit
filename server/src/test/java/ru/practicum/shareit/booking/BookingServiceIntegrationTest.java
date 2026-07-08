package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BookingServiceIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private ItemStorage itemStorage;

    @Autowired
    private BookingStorage bookingStorage;

    @Test
    void createBooking_shouldSaveBooking() {
        User owner = userStorage.save(User.builder()
                .name("Owner")
                .email("owner@mail.ru")
                .build());

        User booker = userStorage.save(User.builder()
                .name("Booker")
                .email("booker@mail.ru")
                .build());

        Item item = itemStorage.save(Item.builder()
                .name("Drill")
                .description("Bosch")
                .available(true)
                .owner(owner)
                .build());

        BookingCreateDto dto = new BookingCreateDto(
                item.getId(),
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        BookingDto booking = bookingService.createBooking(dto, booker.getId());

        assertNotNull(booking.getId());
        assertEquals(item.getId(), booking.getItem().getId());
        assertEquals(booker.getId(), booking.getBooker().getId());
        assertEquals(BookingStatus.WAITING, booking.getStatus());

        Booking bookingFromDb = bookingStorage.findById(booking.getId()).orElseThrow();

        assertEquals(BookingStatus.WAITING, bookingFromDb.getStatus());
        assertEquals(item.getId(), bookingFromDb.getItem().getId());
    }
}

