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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.LoginResponse;
import real.world.domain.user.dto.response.RegisterResponse;
import real.world.domain.user.service.UserService;
import real.world.security.jwt.JwtUtil;

@RestController
public class UserController {

    private static final String AUTH_HEADER = "Authorization";

    private static final String AUTH_TYPE = "BEARER";

    private final JwtUtil jwtUtil;

    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<RegisterResponse> register(
        @RequestBody @Valid RegisterRequest registerRequest) {
        final RegisterResponse response = userService.register(registerRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginResponse> login(Authentication authentication, HttpServletResponse httpServletResponse) {
        final LoginResponse response = userService.login(authentication);

        final String id = authentication.getPrincipal().toString();
        final String authoritiesString = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        final String token = jwtUtil.generateJwtToken(id, authoritiesString);

        httpServletResponse.addHeader(AUTH_HEADER, AUTH_TYPE + " " + token);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/api/profiles")
    public ResponseEntity<String> checkAuth() {
        return new ResponseEntity<>("ok", HttpStatus.CREATED);
    }

}