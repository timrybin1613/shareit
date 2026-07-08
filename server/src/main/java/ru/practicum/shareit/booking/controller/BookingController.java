package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.HeaderConstants;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping
    public List<BookingDto> getBookings(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookingsByUserIdAndBookingState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingService.getBookingsByOwnerIdAndBookingState(userId, state);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingsById(
            @PathVariable Long bookingId,
            @RequestHeader(HeaderConstants.USER_ID) Long userId) {
        return bookingService.getAvailableForUser(bookingId, userId);
    }

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @RequestBody BookingCreateDto dto) {
        return bookingService.createBooking(dto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved) {
        return bookingService.approveBooking(bookingId, userId, approved);
    }
}
