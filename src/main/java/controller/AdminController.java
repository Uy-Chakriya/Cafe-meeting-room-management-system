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
import service.UserService; 
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RoomService roomService;
    private final BookingService bookingService;
    private final UserService userService; 

    @GetMapping("/home")
    public String adminHome(Model model) { 
        long totalRooms = roomService.findAllRooms().size();
        long totalUsers = userService.countAllUsers(); 
        long totalBookings = bookingService.findAllBookings().size(); 

        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalBookings", totalBookings);

        return "admin-home";
    }

    @GetMapping("/rooms")
    public String manageRooms(Model model) {
        List<Room> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        model.addAttribute("room", new Room()); 
        return "admin-room-form"; 
    }

    @PostMapping("/rooms/save")
    public String saveRoom(@ModelAttribute("room") Room room) {
        roomService.saveRoom(room);
        return "redirect:/admin/rooms";
    }
    
    @GetMapping("/rooms/delete/{id}")
    public String deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return "redirect:/admin/rooms";
    }

    @GetMapping("/bookings")
    public String viewAllBookings(Model model) {
        List<Booking> bookings = bookingService.findAllBookings();
        model.addAttribute("bookings", bookings);
        return "admin-bookings"; 
    }
}