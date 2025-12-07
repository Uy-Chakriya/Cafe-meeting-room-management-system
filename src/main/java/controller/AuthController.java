package controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    // GET /login 
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Maps to src/main/resources/templates/login.html
    }
    // POST /login and GET /logout are handled by Spring Security
}