package real.world.domain.user.service;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.response.LoginResponse;
import real.world.domain.user.dto.response.RegisterResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.UsernameAlreadyExistsException;
import real.world.security.JwtUtil;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    @Value("${auth.header}")
    private String AUTH_HEADER;

    @Value("${auth.type}")
    private String AUTH_TYPE;

    @Value("${base.bio}")
    private String BASE_BIO;

    @Value("${base.img.url}")
    private String BASE_IMAGE_URL;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
        User user = userRepository.findById((long) authentication.getPrincipal()).orElseThrow(
            UsernameAlreadyExistsException::new // todo
        );
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

    public MultiValueMap<String, String> getAuthenticationHeader(Authentication authentication) {
        String id = authentication.getPrincipal().toString();
        final String authoritiesString = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));
        final String token = jwtUtil.generateJwtToken(id, authoritiesString);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(AUTH_HEADER, AUTH_TYPE + " " + token);

        return headers;
    }

}
