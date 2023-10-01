package real.world.domain.user.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.UserResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameAlreadyExistsException;

@Service
@Transactional
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

    public UserResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        final User user = requestToEntity(registerRequest);
        userRepository.save(user);
        return UserResponse.of(user);
    }

    public UserResponse getUser(Long id) {
        final User user = findUserById(id);
        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse update(Long id, UpdateRequest request) {
        final User user = findUserById(id);
        final String password = passwordEncode(request.getPassword());
        user.update(
            request.getUsername(),
            password,
            request.getEmail(),
            request.getBio(),
            request.getImage()
        );
        return UserResponse.of(user);
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

    private String passwordEncode(String password) {
        if(password != null) {
            return passwordEncoder.encode(password);
        }
        return null;
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(UserIdNotExistException::new);
    }

}