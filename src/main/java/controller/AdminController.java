package controller;

import entity.Booking;
import entity.Room;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import service.BookingService;
import service.RoomService;

import java.util.List;
// import java.util.Optional; // REMOVED

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RoomService roomService;
    private final BookingService bookingService;

    // GET /admin/home
    @GetMapping("/home")
    public String adminHome() {
        return "admin-home"; // Maps to src/main/resources/templates/admin-home.html
    }

    // GET /admin/rooms (Admin Room Management - Lists rooms)
    @GetMapping("/rooms")
    public String manageRooms(Model model) {
        List<Room> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        // Used to display the CRUD form overlay
        model.addAttribute("room", new Room()); 
        return "admin-room-form"; // Maps to src/main/resources/templates/admin-room-form.html
    }

    // POST /admin/rooms/save (Handles both Add and Edit)
    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }
    
    // GET /admin/rooms/delete/{id}
    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    // GET /admin/bookings (View all bookings)
    @GetMapping("/bookings")
    public String viewAllBookings(Model model) {
        List<Booking> bookings = bookingService.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin-bookings"; // Maps to src/main/resources/templates/admin-bookings.html
    }
}