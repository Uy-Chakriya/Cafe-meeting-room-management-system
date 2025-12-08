package repository;

import entity.Booking;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository; // CORRECTED IMPORT
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// CORRECTED INTERFACE EXTENSION
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // User Feature: View only their bookings
    List<Booking> findByUserIdOrderByDateAscStartTimeAsc(Long userId);

    // Business Logic: Check for time conflicts during a new booking
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId AND b.date = :date AND " +
           "((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("date") LocalDate date,
                                          @Param("startTime") LocalTime startTime,
                                          @Param("endTime") LocalTime endTime);
}