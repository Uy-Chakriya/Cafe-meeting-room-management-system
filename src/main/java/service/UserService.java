package service;

import entity.User;
import entity.User.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repository.UserRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // NOTE: The initializeDefaultUsers() method with @PostConstruct 
    // has been removed here, as the logic is now handled by CommandLineRunner
    // in CafeMeetingRoomBookingSystemApplication.java.

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Method to register a new user (used by AuthController and CommandLineRunner)
    public User registerUser(User user) {
        // Check for username uniqueness
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        
        // Encode password and set default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Ensure the role is set if it hasn't been explicitly set (e.g., if creating a regular user)
        if (user.getRole() == null) {
            user.setRole(Role.USER); 
        }
        
        return userRepository.save(user);
    }
}