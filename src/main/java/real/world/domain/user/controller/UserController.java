package real.world.domain.user.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import real.world.domain.user.service.UserService;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.RegisterResponse;

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

        return ResponseEntity.ok(response);
    }

}
