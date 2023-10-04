package real.world.config;

import java.util.List;
import java.util.stream.Stream;
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
import real.world.security.authentication.CustomAuthenticationProvider;
import real.world.security.authentication.CustomUsernamePasswordAuthenticationFilter;
import real.world.security.handler.CustomAuthenticationFailureHandler;
import real.world.security.jwt.JwtAuthenticationFilter;
import real.world.security.jwt.JwtAuthenticationProvider;
import real.world.security.service.UserDetailsByEmailService;
import real.world.security.service.UserDetailsByIdService;
import real.world.security.support.JwtUtil;
import real.world.security.support.NorRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private static final String LOGIN_PATH = "/users/login";
    private static final String[] AUTH_PATH = {"/users", LOGIN_PATH};
    private static final String[] SWAGGER_PATH = {"/docs/open-api.json", "/swagger-ui/**",
        "/v3/**"};
    private static final String[] OPTIONAL_PATH = {"/profiles/*", "/articles/**"};

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
                .requestMatchers(HttpMethod.GET, SWAGGER_PATH).permitAll()
                .requestMatchers(HttpMethod.GET, OPTIONAL_PATH).hasAnyRole("USER", "ANONYMOUS")
                .anyRequest().hasRole("USER")
            )
            .addFilterBefore(customAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter(), AuthorizationFilter.class)
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
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
            .authenticationProvider(
                new CustomAuthenticationProvider(userDetailsByEmailService, passwordEncoder()))
            .authenticationProvider(new JwtAuthenticationProvider(userDetailsByIdService, jwtUtil))
            .build();
    }

    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    public CustomUsernamePasswordAuthenticationFilter customAuthenticationFilter()
        throws Exception {
        final CustomUsernamePasswordAuthenticationFilter filter =
            new CustomUsernamePasswordAuthenticationFilter(LOGIN_PATH, authenticationManager());
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());

        return filter;
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        final String[] norPath = Stream.of(AUTH_PATH, SWAGGER_PATH)
            .flatMap(Stream::of)
            .toArray(String[]::new);
        final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(
            new NorRequestMatcher(norPath), authenticationManager(), OPTIONAL_PATH);
        filter.setAuthenticationFailureHandler(customAuthenticationFailureHandler());

        return filter;
    }

}