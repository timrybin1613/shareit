package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void getBookings_shouldReturnBookings() throws Exception {

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getBookingsByUserIdAndBookingState(1L, "ALL"))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"));

        verify(bookingService)
                .getBookingsByUserIdAndBookingState(1L, "ALL");
    }

    @Test
    void getBookingsByOwner_shouldReturnOwnerBookings() throws Exception {

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getBookingsByOwnerIdAndBookingState(1L, "ALL"))
                .thenReturn(List.of(booking));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("WAITING"));

        verify(bookingService)
                .getBookingsByOwnerIdAndBookingState(1L, "ALL");
    }

    @Test
    void getBookingById_shouldReturnBooking() throws Exception {

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.getAvailableForUser(1L, 1L))
                .thenReturn(booking);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(bookingService)
                .getAvailableForUser(1L, 1L);
    }

    @Test
    void createBooking_shouldReturnCreatedBooking() throws Exception {

        BookingCreateDto request = new BookingCreateDto();
        request.setItemId(1L);
        request.setStart(LocalDateTime.now().plusDays(1));
        request.setEnd(LocalDateTime.now().plusDays(2));

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.WAITING)
                .build();

        when(bookingService.createBooking(any(BookingCreateDto.class), eq(1L)))
                .thenReturn(booking);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));

        verify(bookingService)
                .createBooking(any(BookingCreateDto.class), eq(1L));
    }

    @Test
    void approveBooking_shouldReturnApprovedBooking() throws Exception {

        BookingDto booking = BookingDto.builder()
                .id(1L)
                .status(BookingStatus.APPROVED)
                .build();

        when(bookingService.approveBooking(1L, 1L, true))
                .thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(bookingService)
                .approveBooking(1L, 1L, true);
    }
}

