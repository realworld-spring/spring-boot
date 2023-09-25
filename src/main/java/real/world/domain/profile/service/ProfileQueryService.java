package real.world.domain.profile.service;

import org.springframework.stereotype.Service;
import real.world.domain.profile.dto.response.ProfileDto;
import real.world.domain.profile.query.ProfileQueryRepository;
import real.world.domain.profile.query.Profile;
import real.world.error.exception.UsernameNotExistException;

@Service
public class ProfileQueryService {

    private final ProfileQueryRepository profileQueryRepository;

    public ProfileQueryService(ProfileQueryRepository profileQueryRepository) {
        this.profileQueryRepository = profileQueryRepository;
    }

    public ProfileDto getProfile(Long loginId, String username) {
        final Profile profile = profileQueryRepository.findByLoginIdAndUsername(loginId, username)
            .orElseThrow(UsernameNotExistException::new);
        return ProfileDto.of(profile);
    }

}