package service;

import entity.Booking;
import entity.Room;
import entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import repository.BookingRepository;
import repository.RoomRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;

    // User: Book a room
    @Transactional
    public Booking bookRoom(Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        // 1. Check if room exists
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));

        // 2. Check for time conflicts
        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, date, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("The room is already booked during the requested time slot.");
        }

        // 3. Create and save the booking
        User user = new User(); 
        user.setId(userId);

        Booking newBooking = new Booking();
        newBooking.setUser(user);
        newBooking.setRoom(room);
        newBooking.setDate(date);
        newBooking.setStartTime(startTime);
        newBooking.setEndTime(endTime);

        return bookingRepository.save(newBooking);
    }

    // User: View only their bookings
    public List<Booking> findUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByDateAscStartTimeAsc(userId);
    }

    // Admin: View all bookings
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }
}