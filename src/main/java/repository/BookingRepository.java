package repository;

import entity.Booking;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

// FIX: Import the correct Spring Data JPA repository
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// FIX: Extend JpaRepository instead of the CDI extension
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