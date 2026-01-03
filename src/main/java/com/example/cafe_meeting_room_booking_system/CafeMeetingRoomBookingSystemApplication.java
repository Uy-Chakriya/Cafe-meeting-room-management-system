package com.example.cafe_meeting_room_booking_system;

import entity.User; 
import service.UserService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"controller", "service", "repository", "security", "com.example.cafe_meeting_room_booking_system"}) 
@EnableJpaRepositories(basePackages = "repository")
public class CafeMeetingRoomBookingSystemApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(CafeMeetingRoomBookingSystemApplication.class, args);
        System.out.println("=== Application Started ===");
        System.out.println("Open the application on its configured port (default is 8080)");
    }


         // username :  admin
        //  password: adminpass
    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Checking for default users ===");

        if (userService.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("adminpass"); 
            admin.setRole(User.Role.ADMIN); 
            userService.registerUser(admin);
            System.out.println("✓ Default admin user created: admin/adminpass");
        } else {
            System.out.println("✓ Admin user already exists");
        }


        // username : user
        //  password: userpass

        if (userService.findByUsername("user").isEmpty()) {
            User user = new User();
            user.setUsername("user");
            user.setPassword("userpass");
            user.setRole(User.Role.USER);
            userService.registerUser(user);
            System.out.println("✓ Default regular user created: user/userpass");
        } else {
            System.out.println("✓ Regular user already exists");
        }
    }
}