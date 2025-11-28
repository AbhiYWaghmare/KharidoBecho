//package com.spring.jwt.Bike.Mapper;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(
//                                "/ws/**",
//                                "/ws",
//                                "/ws/info/**",
//                                "/topic/**",
//                                "/app/**",
//                                "/bikes/ws/**"
//                        ).permitAll()
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(httpBasic -> httpBasic.disable())   // Disable Basic Auth
//                .formLogin(form -> form.disable())             // Disable login form
//                .logout(logout -> logout.disable());
//
//
//        return http.build();
//    }
//}
//
