package real.world.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.LoginResponse;
import real.world.domain.user.dto.response.RegisterResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameAlreadyExistsException;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${base.bio}")
    private String BASE_BIO;

    @Value("${base.img.url}")
    private String BASE_IMAGE_URL;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        final User user = requestToEntity(registerRequest);
        userRepository.save(user);
        return RegisterResponse.of(user);
    }

    public LoginResponse login(Authentication authentication) {
        User user = userRepository.findById((long) authentication.getPrincipal())
            .orElseThrow(UserIdNotExistException::new);
        return LoginResponse.of(user);
    }

    private User requestToEntity(RegisterRequest registerRequest) {
        return new User(
            registerRequest.getUsername(),
            passwordEncoder.encode(registerRequest.getPassword()),
            registerRequest.getEmail(),
            BASE_BIO,
            BASE_IMAGE_URL
        );
    }

}
