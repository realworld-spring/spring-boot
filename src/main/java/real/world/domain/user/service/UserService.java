package real.world.domain.user.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.ProfileResponse;
import real.world.domain.user.dto.response.UserResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.entity.UserRole;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.AlreadyFollowedException;
import real.world.error.exception.FollowNotExistException;
import real.world.error.exception.RecursiveFollowException;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameAlreadyExistsException;
import real.world.error.exception.UsernameNotExistException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final FollowRepository followRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${base.bio}")
    private String BASE_BIO;

    @Value("${base.img.url}")
    private String BASE_IMAGE_URL;

    public UserService(UserRepository userRepository, FollowRepository followRepository,
        PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
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

    public ProfileResponse follow(Long followerId, String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final User follower = userRepository.findById(followerId)
            .orElseThrow(UserIdNotExistException::new);

        if(user.getId().equals(followerId)) { throw new RecursiveFollowException(); }
        if(followRepository.existsByUserIdAndFollowerId(user.getId(), followerId)) {
            throw new AlreadyFollowedException();
        }

        final Follow follow = new Follow(user, follower);
        followRepository.save(follow);
        return ProfileResponse.of(user, true);
    }

    public ProfileResponse unfollow(Long followerId, String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final User follower = userRepository.findById(followerId)
            .orElseThrow(UserIdNotExistException::new);

        if(user.getId().equals(followerId)) { throw new RecursiveFollowException(); }
        if(!followRepository.existsByUserIdAndFollowerId(user.getId(), followerId)) {
            throw new FollowNotExistException();
        }

        final Follow follow = new Follow(user, follower);
        followRepository.delete(follow);
        return ProfileResponse.of(user, false);
    }

    public ProfileResponse getProfile(String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final boolean following = isFollowing(user.getId());
        return ProfileResponse.of(user, following);
    }

    private boolean isFollowing(Long userId) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(hasAuthority(authentication)) {
            final Long followerId = Long.valueOf((String) authentication.getPrincipal());
            return followRepository.existsByUserIdAndFollowerId(userId, followerId);
        }
        return false;
    }

    private boolean hasAuthority(Authentication authentication) {
        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        final GrantedAuthority requiredAuthority = new SimpleGrantedAuthority(UserRole.ROLE_USER.toString());
        return authorities.contains(requiredAuthority);
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