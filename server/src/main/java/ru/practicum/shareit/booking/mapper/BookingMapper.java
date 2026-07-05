package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@AllArgsConstructor
public class BookingMapper {

    private final UserMapper userMapper;
    private final  ItemMapper itemMapper;

    public Booking toBooking(BookingCreateDto dto, Item item, User booker, BookingStatus status) {
        return Booking.builder()
                .booker(booker)
                .item(item)
                .start(dto.getStart())
                .end(dto.getEnd())
                .status(status)
                .build();
    }

    public BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(userMapper.toUserDto(booking.getBooker()))
                .item(itemMapper.toItemDto(booking.getItem()))
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public List<BookingDto> toBookingsDtos(List<Booking> bookings) {
        return bookings.stream()
                .map(this::toBookingDto)
                .toList();
    }

}
