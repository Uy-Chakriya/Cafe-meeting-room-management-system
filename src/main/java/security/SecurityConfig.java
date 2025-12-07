package security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Public access (CSS, Login Page, H2 Console)
                .requestMatchers(
                    "/", "/login", "/css/**", "/js/**", "/images/**",
                    "/h2-console/**", "/error"
                ).permitAll()
                // Admin access control
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Authenticated users (USER and ADMIN) can access user features
                .requestMatchers("/rooms/**", "/user/bookings", "/book", "/calendar").authenticated()
                // Deny all other requests
                .anyRequest().denyAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/rooms", true) // Redirect to room list on success
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .permitAll()
                .logoutSuccessUrl("/login?logout")
            )
            .csrf((csrf) -> csrf
                // Disable CSRF for H2 console frame for development
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers((headers) -> headers
                // Allow H2 console to be displayed in a frame
                .frameOptions((frame) -> frame.sameOrigin())
            );

        return http.build();
    }
}