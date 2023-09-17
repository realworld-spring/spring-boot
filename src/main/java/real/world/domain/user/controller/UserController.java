package real.world.domain.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.auth.annotation.Auth;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.UserResponse;
import real.world.domain.user.service.UserService;
import real.world.security.support.JwtUtil;

@RestController
public class UserController {

    private static final String AUTH_HEADER = "Authorization";

    private static final String AUTH_TYPE = "Token";

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<UserResponse> register(
        @RequestBody @Valid RegisterRequest registerRequest) {
        final UserResponse response = userService.register(registerRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<UserResponse> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        final String id = authentication.getPrincipal().toString();
        final UserResponse response = userService.login(Long.valueOf(id));

        final String authoritiesString = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        final String token = jwtUtil.generateJwtToken(id, authoritiesString);

        httpServletResponse.addHeader(AUTH_HEADER, AUTH_TYPE + " " + token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/user")
    public ResponseEntity<UserResponse> currentUser(@Auth Long loginId) {
        final UserResponse response = userService.getUser(loginId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/api/user")
    public ResponseEntity<UserResponse> update(@Auth Long loginId,
        @RequestBody UpdateRequest updateRequest) {
        final UserResponse response = userService.update(loginId, updateRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/profiles")
    public ResponseEntity<String> checkAuth(@Auth Long loginId) {
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

}