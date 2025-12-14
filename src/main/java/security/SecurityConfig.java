package security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .requestMatchers(
                    "/", "/login", "/css/**", "/js/**", "/images/**",
                    "/h2-console/**", "/error"
                ).permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/rooms/**", "/user/bookings", "/book", "/calendar").authenticated()
                .anyRequest().denyAll()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .defaultSuccessUrl("/rooms", true) 
            )
            .logout((logout) -> logout
                .logoutRequestMatcher(request -> request.getServletPath().equals("/logout"))
                .permitAll()
                .logoutSuccessUrl("/login?logout")
            )
            .csrf((csrf) -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .headers((headers) -> headers

                .frameOptions((frame) -> frame.sameOrigin())
            );

        return http.build();
    }
}