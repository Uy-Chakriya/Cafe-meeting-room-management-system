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

    @Transactional
    public Booking bookRoom(Long userId, Long roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found."));

        List<Booking> conflicts = bookingRepository.findConflictingBookings(roomId, date, startTime, endTime);
        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("The room is already booked during the requested time slot.");
        }

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

    public List<Booking> findUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByDateAscStartTimeAsc(userId);
    }


    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }
}