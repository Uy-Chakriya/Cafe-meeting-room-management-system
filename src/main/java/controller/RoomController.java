package controller;

import entity.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import service.RoomService;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @GetMapping({"/", "/rooms"})
    public String listRooms(Model model) {
        List<Room> rooms = roomService.findAllRooms();
        model.addAttribute("rooms", rooms);
        return "user-home"; 
    }

    @GetMapping("/rooms/{id}")
    public String viewRoomDetail(@PathVariable Long id, Model model) {
        Optional<Room> roomOpt = roomService.findRoomById(id);
        if (roomOpt.isEmpty()) {
            return "redirect:/rooms";
        }
        model.addAttribute("room", roomOpt.get());
        return "book-room";
    }
}