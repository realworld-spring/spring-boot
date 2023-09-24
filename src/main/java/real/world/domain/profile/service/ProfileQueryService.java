package real.world.domain.profile.service;

import org.springframework.stereotype.Service;
import real.world.domain.profile.dto.response.ProfileResponse;
import real.world.domain.profile.repository.ProfileQueryRepository;
import real.world.domain.user.dto.ProfileDto;
import real.world.error.exception.UsernameNotExistException;

@Service
public class ProfileQueryService {

    private final ProfileQueryRepository profileQueryRepository;

    public ProfileQueryService(ProfileQueryRepository profileQueryRepository) {
        this.profileQueryRepository = profileQueryRepository;
    }

    public ProfileResponse getProfile(String username, Long followerId) {
        final ProfileDto profile = profileQueryRepository.findByIdAndUsername(followerId, username)
            .orElseThrow(UsernameNotExistException::new); // TODO
        return ProfileResponse.of(profile);
    }

}