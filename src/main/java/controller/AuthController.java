package controller;

import entity.User; 
import lombok.RequiredArgsConstructor; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.UserService; 

@Controller
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;

    // Redirect root to login
    @GetMapping("/")
    public String home() {
        return "redirect:/login"; 
    }

    // GET /login - Handles error/logout messages from Spring Security
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login"; // Maps to src/main/resources/templates/login.html
    }

    // GET /register - Shows the registration form (you need a register.html template)
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; 
    }

    // POST /register - Processes the registration request
    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            
            userService.registerUser(user);
            
            // On success, display success message on the login page
            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (RuntimeException e) {
            // On failure (e.g., username exists), return to register page with error
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}