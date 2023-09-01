package real.world.domain.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.user.dto.request.LoginRequest;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.LoginResponse;
import real.world.domain.user.dto.response.RegisterResponse;
import real.world.domain.user.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/users")
    public ResponseEntity<RegisterResponse> register(
        @RequestBody @Valid RegisterRequest registerRequest) {
        final RegisterResponse response = userService.register(registerRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/api/users/login")
    public ResponseEntity<LoginResponse> login(
        @RequestBody @Valid LoginRequest loginRequest) {
        final LoginResponse response  = userService.login(loginRequest);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
