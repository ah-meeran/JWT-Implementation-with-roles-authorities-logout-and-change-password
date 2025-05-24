package com.example.demo.config.security;

import com.example.demo.model.permission.Permission;
import com.example.demo.model.permission.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // Disabling CSRF protection
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/api/v1/health").permitAll()
                        .requestMatchers("/demo/**").authenticated()
                        .requestMatchers("/api/v1/admin/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_WRITE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/admin/**")
                        .hasAuthority(Permission.ADMIN_DELETE.name())
                        .requestMatchers("/api/v1/manager/**")
                        .hasAnyRole(Role.ADMIN.name(), Role.MANAGER.name())
                        .requestMatchers(HttpMethod.GET, "/api/v1/manager/**")
                        .hasAnyAuthority(Permission.MANAGER_READ.name(),
                                Permission.ADMIN_READ.name())
                        .requestMatchers(HttpMethod.POST, "/api/v1/manager/**")
                        .hasAnyAuthority(Permission.MANAGER_WRITE.name(),
                                Permission.ADMIN_WRITE.name())
                        .requestMatchers(HttpMethod.PUT, "/api/v1/manager/**")
                        .hasAnyAuthority(Permission.MANAGER_UPDATE.name(),
                                Permission.ADMIN_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/manager/**")
                        .hasAnyAuthority(Permission.MANAGER_DELETE.name(),
                                Permission.ADMIN_DELETE.name())
                        .anyRequest().authenticated() // Any other request must be authenticated
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/authendicate")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> {
                            SecurityContextHolder.clearContext();
                        }));

        return http.build();
    }

    // @Bean
    // public CorsConfigurationSource corsConfigurationSource() {
    // CorsConfiguration configuration = new CorsConfiguration();
    // configuration.addAllowedOrigin("*");
    // configuration.addAllowedMethod("*");
    // configuration.addAllowedHeader("*");

    // UrlBasedCorsConfigurationSource source = new
    // UrlBasedCorsConfigurationSource();
    // source.registerCorsConfiguration("/**", configuration);
    // return source;
    // }
}
