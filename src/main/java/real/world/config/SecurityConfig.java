package real.world.config;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import real.world.security.CustomAuthenticationProvider;
import real.world.security.NorRequestMatcher;
import real.world.security.JwtAuthenticationFilter;
import real.world.security.JwtAuthenticationProvider;
import real.world.security.CustomLoginFailureHandler;
import real.world.security.CustomUsernamePasswordAuthenticationFilter;
import real.world.security.JwtUtil;
import real.world.security.UserDetailsByEmailService;
import real.world.security.UserDetailsByIdService;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String AUTH_PATH = "/api/users";
    private static final String LOGIN_PATH = "/api/users/login";

    private final ObjectPostProcessor<Object> objectPostProcessor;
    private final UserDetailsByEmailService userDetailsByEmailService;
    private final UserDetailsByIdService userDetailsByIdService;

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, AUTH_PATH).permitAll()
                .anyRequest().hasRole("USER")
            )
            .addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter(), AuthorizationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOriginPatterns(List.of("*"));
        cors.setAllowedMethods(List.of("*"));
        cors.setAllowedHeaders(List.of("*"));
        cors.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManagerBuilder(objectPostProcessor)
            .authenticationProvider(new CustomAuthenticationProvider(userDetailsByEmailService, passwordEncoder()))
            .authenticationProvider(new JwtAuthenticationProvider(userDetailsByIdService, jwtUtil))
            .build();
    }

    public CustomLoginFailureHandler customLoginFailureHandler() {
        return new CustomLoginFailureHandler();
    }

    public CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomUsernamePasswordAuthenticationFilter customUsernamePasswordAuthenticationFilter =
            new CustomUsernamePasswordAuthenticationFilter(LOGIN_PATH, authenticationManager());
        customUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(customLoginFailureHandler());

        return customUsernamePasswordAuthenticationFilter;
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        String[] norPath = {AUTH_PATH, LOGIN_PATH};
        return new JwtAuthenticationFilter(new NorRequestMatcher(norPath), authenticationManager());
    }

}