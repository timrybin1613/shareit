package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingCreateDto dto, Long userId);

    BookingDto approveBooking(Long bookingId, Long userId, boolean approved);

    List<BookingDto> getBookingsByUserIdAndBookingState(Long userId, String state);

    List<BookingDto> getBookingsByOwnerIdAndBookingState(Long userId, String state);

    BookingDto getAvailableForUser(Long bookingId, Long userId);

    LocalDateTime getLastBookingForItem(Long itemId, LocalDateTime now);

    LocalDateTime getNextBookingForItem(Long itemId, LocalDateTime now);
}
