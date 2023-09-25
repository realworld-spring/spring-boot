package real.world.domain.follow.service;

import org.springframework.stereotype.Service;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.profile.dto.response.ProfileResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.AlreadyFollowingException;
import real.world.error.exception.FollowNotExistException;
import real.world.error.exception.RecursiveFollowException;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameNotExistException;

@Service
public class FollowService {

    private final UserRepository userRepository;

    private final FollowRepository followRepository;

    public FollowService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public ProfileResponse follow(Long loginId, String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final User loginUser = userRepository.findById(loginId)
            .orElseThrow(UserIdNotExistException::new);

        if(user.getId().equals(loginId)) { throw new RecursiveFollowException(); }
        if(followRepository.existsByUserIdAndFollowerId(user.getId(), loginId)) {
            throw new AlreadyFollowingException();
        }

        final Follow follow = new Follow(user, loginUser);
        followRepository.save(follow);
        return ProfileResponse.of(user, true);
    }

    public ProfileResponse unfollow(Long loginId, String username) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final User loginUser = userRepository.findById(loginId)
            .orElseThrow(UserIdNotExistException::new);

        if(user.getId().equals(loginId)) { throw new RecursiveFollowException(); }
        if(!followRepository.existsByUserIdAndFollowerId(user.getId(), loginId)) {
            throw new FollowNotExistException();
        }

        final Follow follow = new Follow(user, loginUser);
        followRepository.delete(follow);
        return ProfileResponse.of(user, false);
    }

}