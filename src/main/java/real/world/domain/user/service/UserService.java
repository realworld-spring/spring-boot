package real.world.domain.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.UserDto;
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

    public UserDto register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }
        final User user = requestToEntity(registerRequest);
        userRepository.save(user);
        return UserDto.of(user);
    }

    public UserDto getUser(Long id) {
        final User user = findUserById(id);
        return UserDto.of(user);
    }

    @Transactional
    public UserDto update(Long id, UpdateRequest request) {
        final User user = findUserById(id);
        final String password = passwordEncode(request.getPassword());
        user.update(
            request.getUsername(),
            password,
            request.getEmail(),
            request.getBio(),
            request.getImage()
        );
        return UserDto.of(user);
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