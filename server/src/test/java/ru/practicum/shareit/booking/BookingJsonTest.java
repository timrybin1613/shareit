package ru.practicum.shareit.booking;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class BookingJsonTest {
    private Validator validator;

    @BeforeEach
    void setUp() {

        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void validDto_shouldHaveNoViolations() {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty());
    }

    @Test
    void itemIdIsNull_shouldHaveViolation() {
        BookingCreateDto dto = new BookingCreateDto(
                null,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("itemId", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void startIsNull_shouldHaveViolation() {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                null,
                LocalDateTime.now().plusDays(2)
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("start", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void endIsNull_shouldHaveViolation() {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                LocalDateTime.now().plusDays(1),
                null
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("end", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void startInPast_shouldHaveViolation() {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("start", violations.iterator().next().getPropertyPath().toString());
    }

    @Test
    void endInPast_shouldHaveViolation() {
        BookingCreateDto dto = new BookingCreateDto(
                1L,
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().minusDays(1)
        );

        Set<ConstraintViolation<BookingCreateDto>> violations = validator.validate(dto);

        assertEquals(1, violations.size());
        assertEquals("end", violations.iterator().next().getPropertyPath().toString());
    }
}
