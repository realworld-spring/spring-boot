package real.world.domain.follow.service;

import org.springframework.stereotype.Service;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.follow.dto.response.ProfileResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.error.exception.AlreadyFollowedException;
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

    public ProfileResponse getProfile(String username, Long followerId) {
        final User user = userRepository.findByUsername(username)
            .orElseThrow(UsernameNotExistException::new);
        final boolean following = isFollowing(user.getId(), followerId);
        return ProfileResponse.of(user, following);
    }

    private boolean isFollowing(Long userId, Long followerId) {
        return followerId != 0L && followRepository.existsByUserIdAndFollowerId(userId, followerId);
    }

}