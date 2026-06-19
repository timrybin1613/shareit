package ru.practicum.shareit.booking.service;

import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {
    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto createBooking(BookingCreateDto dto, Long userId) {
        Item item = itemStorage.findById(dto.getItemId())
                .orElseThrow(() -> new NotFoundException("Item not found"));
        User booker = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (!dto.getStart().isBefore(dto.getEnd())) {
            throw new ValidationException("Start must be before end");
        }

        if (!item.getAvailable()) {
            throw new ValidationException("Item is unavailable");
        }

        Booking booking = bookingMapper.toBooking(dto, item, booker, BookingStatus.WAITING);

        return bookingMapper.toBookingDto(bookingStorage.save(booking));
    }

    @Override
    @Transactional
    public BookingDto approveBooking(Long bookingId, Long userId, boolean approved) {
        Booking booking = bookingStorage.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking not found"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("User is not owner of this booking");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByUserIdAndBookingState(Long userId, String state) {
        validateUserByUserId(userId);
        BookingState bookingState = parseBookingStateOrThrow(state);
        LocalDateTime now = LocalDateTime.now();

        return switch (bookingState) {
            case ALL -> bookingMapper.toBookingsDtos(bookingStorage.findByBookerIdOrderByStartDesc(userId));

            case CURRENT -> bookingMapper.toBookingsDtos(bookingStorage.findCurrentBookingsByBookerId(userId, now));

            case PAST -> bookingMapper.toBookingsDtos(bookingStorage.findPastBookingsByBookerId(userId, now));

            case FUTURE -> bookingMapper.toBookingsDtos(bookingStorage.findFutureBookingsByBookerId(userId, now));

            case WAITING -> bookingMapper.toBookingsDtos(bookingStorage.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.WAITING));

            case REJECTED -> bookingMapper.toBookingsDtos(bookingStorage.findByBookerIdAndStatusOrderByStartDesc(userId,
                        BookingStatus.REJECTED));
        };
    }

    @Override
    public List<BookingDto> getBookingsByOwnerIdAndBookingState(Long ownerId, String state) {
        validateUserByUserId(ownerId);
        BookingState bookingState = parseBookingStateOrThrow(state);
        LocalDateTime now = LocalDateTime.now();

        return switch (bookingState) {
            case ALL -> bookingMapper.toBookingsDtos(bookingStorage.findByItemOwnerIdOrderByStartDesc(ownerId));

            case CURRENT -> bookingMapper.toBookingsDtos(bookingStorage.findCurrentBookingsByOwnerId(ownerId, now));

            case PAST -> bookingMapper.toBookingsDtos(bookingStorage.findPastBookingsByOwnerId(ownerId, now));

            case FUTURE -> bookingMapper.toBookingsDtos(bookingStorage.findFutureBookingsByOwnerId(ownerId, now));

            case WAITING -> bookingMapper.toBookingsDtos(bookingStorage.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId,
                    BookingStatus.WAITING));

            case REJECTED -> bookingMapper.toBookingsDtos(bookingStorage.findByItemOwnerIdAndStatusOrderByStartDesc(ownerId,
                    BookingStatus.REJECTED));
        };
    }

    @Override
    public BookingDto getAvailableForUser(Long bookingId, Long userId) {
        return bookingMapper.toBookingDto(bookingStorage.findAvailableForUser(bookingId, userId)
                .orElseThrow(() -> new NotFoundException("Booking not found")));
    }

    @Override
    public LocalDateTime getLastBookingForItem(Long itemId, LocalDateTime now) {
        return bookingStorage.getLastBookingDate(itemId, now);
    }

    @Override
    public LocalDateTime getNextBookingForItem(Long itemId, LocalDateTime now) {
        return bookingStorage.getNextBookingByItemId(itemId, now);
    }

    private void validateUserByUserId(Long userId) {
        userStorage.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private BookingState parseBookingStateOrThrow(String state) {
        try {
            return BookingState.valueOf(state.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Unknown state: " + state);
        }
    }
}
