package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.util.HeaderConstants;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingClient.getBookings(userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        return bookingClient.getBookingsByOwner(userId, state);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(HeaderConstants.USER_ID) long userId,
                                           @RequestBody @Valid BookingCreateDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader(HeaderConstants.USER_ID) long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader(HeaderConstants.USER_ID) Long userId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved) {
        return bookingClient.approveBooking(userId, bookingId, approved);
    }
}