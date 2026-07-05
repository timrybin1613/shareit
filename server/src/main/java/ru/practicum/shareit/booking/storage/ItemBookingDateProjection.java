package ru.practicum.shareit.booking.storage;


import java.time.LocalDateTime;

public interface ItemBookingDateProjection {
    Long getItemId();

    LocalDateTime getDateBooking();
}
