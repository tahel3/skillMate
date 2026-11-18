package com.example.hobbiesmanagment.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private com.example.hobbiesmanagment.Jwt.JwtFilter jwtFilter;

    // Extra allowed origins for production, comma separated (e.g. https://skillmate.vercel.app)
    @Value("${app.cors.allowed-origins:}")
    private String extraAllowedOrigins;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfiguration = new CorsConfiguration();
                    List<String> origins = new ArrayList<>(List.of(
                            "http://localhost:*",
                            "http://127.0.0.1:*"
                    ));
                    Arrays.stream(extraAllowedOrigins.split(","))
                            .map(String::trim)
                            .filter(o -> !o.isEmpty())
                            .forEach(origins::add);
                    corsConfiguration.setAllowedOriginPatterns(origins);
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/skill/search").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/mentors").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/mentors/{id}").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/mentors/{id}/available-dates").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/mentors/{id}/skills").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/skill/by-mentor/{mentorId}").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/mentors/me").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/mentors/*/availability").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/mentors/*/add-skill").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/mentors/*/skills/*").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/mentors/my-skills").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/mentors/my-skills/**").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/mentors/*/add-student").hasAnyRole("MENTOR","MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/reviews/mentor/**").hasAnyRole("MENTOR", "MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/learners/**").hasAnyRole("MENTOR", "LEARNER",  "MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/learners/*/favorites/*").hasAnyRole("LEARNER", "MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/learners/*/favorites/*").hasAnyRole("LEARNER", "MENTOR_AND_LEARNER")
                        .requestMatchers("/ws-chat/**").permitAll()
                        .requestMatchers("/ws-chat/info").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/chat/conversations/**").hasAnyRole("MENTOR", "LEARNER", "MENTOR_AND_LEARNER")
                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/ai/ask").hasAnyRole("MENTOR", "LEARNER", "MENTOR_AND_LEARNER")
                        .requestMatchers("/api/learners/**").hasAnyRole("LEARNER", "MENTOR_AND_LEARNER")
                        .anyRequest().hasAnyRole("MENTOR", "LEARNER", "MENTOR_AND_LEARNER")
                )
                .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
