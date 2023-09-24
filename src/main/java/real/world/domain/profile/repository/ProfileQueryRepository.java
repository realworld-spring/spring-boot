package real.world.domain.profile.repository;

import java.util.Optional;
import real.world.domain.user.dto.ProfileDto;

public interface ProfileQueryRepository {

    Optional<ProfileDto> findByIdAndUsername(Long loginId, String username);

}