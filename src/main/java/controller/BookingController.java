package controller;

import entity.Booking;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import repository.UserRepository;
import service.BookingService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final UserRepository userRepository; 

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() instanceof String) {
            throw new SecurityException("User not authenticated.");
        }
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new SecurityException("Authenticated user not found in database."));
    }

    // POST /book (User: Book a room)
    @PostMapping("/book")
    public String bookRoom(@RequestParam Long roomId,
                           @RequestParam LocalDate date,
                           @RequestParam LocalTime startTime,
                           @RequestParam LocalTime endTime,
                           Model model) {
        try {
            Long userId = getCurrentUserId();
            bookingService.bookRoom(userId, roomId, date, startTime, endTime);
            return "redirect:/user/bookings?success"; 
        } catch (Exception e) {
            // In a real app, you'd add a flash attribute here
            System.err.println("Booking Error: " + e.getMessage());
            return "redirect:/rooms/" + roomId + "?error=" + e.getMessage();
        }
    }

    // GET /my-bookings (User: View only their bookings)
    @GetMapping("/user/bookings")
    public String viewMyBookings(Model model) {
        try {
            Long userId = getCurrentUserId();
            List<Booking> bookings = bookingService.findUserBookings(userId);
            model.addAttribute("bookings", bookings);
            return "my-bookings"; // Maps to src/main/resources/templates/my-bookings.html
        } catch (SecurityException e) {
            return "redirect:/login";
        }
    }

    // GET /calendar (User: Calendar view, often linked from /rooms/{id})
    @GetMapping("/calendar")
    public String viewCalendar() {
        return "book-room"; // Maps to src/main/resources/templates/book-room.html
    }
}