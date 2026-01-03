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

    @GetMapping("/")
    public String home() {
        return "redirect:/login"; 
    }

    

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
    
    return "login";
}

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; 
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            
            userService.registerUser(user);

            model.addAttribute("success", "Registration successful! Please login.");
            return "login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}