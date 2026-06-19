package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingStorage extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);


    boolean existsByBookerIdAndItem_IdAndStatusAndEndLessThanEqual(
            Long userId,
            Long itemId,
            BookingStatus status,
            LocalDateTime now
    );

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.start <= :now
              AND b.end >= :now
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentBookingsByBookerId(Long userId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.end < :now
            ORDER BY b.start DESC
            """)
    List<Booking> findPastBookingsByBookerId(Long userId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.booker.id = :userId
              AND b.start > :now
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureBookingsByBookerId(Long userId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.id = :bookingId
            AND (
                b.booker.id = :userId
                OR b.item.owner.id = :userId
            )
            """)
    Optional<Booking> findAvailableForUser(Long bookingId, Long userId);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start <= :now
              AND b.end >= :now
            ORDER BY b.start DESC
            """)
    List<Booking> findCurrentBookingsByOwnerId(Long ownerId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.end < :now
            ORDER BY b.start DESC
            """)
    List<Booking> findPastBookingsByOwnerId(Long ownerId, LocalDateTime now);

    @Query("""
            SELECT b
            FROM Booking b
            WHERE b.item.owner.id = :ownerId
              AND b.start > :now
            ORDER BY b.start DESC
            """)
    List<Booking> findFutureBookingsByOwnerId(Long ownerId, LocalDateTime now);

    @Query("""
            SELECT MAX(b.start)
            FROM Booking b
            WHERE b.item.id = :itemId
              AND b.start < :now
              AND b.status = 'APPROVED'
            """)
    LocalDateTime getLastBookingDate(Long itemId, LocalDateTime now);

    @Query("""
            SELECT MIN(b.start)
            FROM Booking b
            WHERE b.item.id = :itemId
              AND b.start > :now
              AND b.status = 'APPROVED'
            """)
    LocalDateTime getNextBookingByItemId(Long itemId, LocalDateTime now);
}
