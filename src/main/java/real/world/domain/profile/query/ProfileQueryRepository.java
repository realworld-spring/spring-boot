package real.world.domain.profile.query;

import java.util.Optional;

public interface ProfileQueryRepository {

    Optional<Profile> findByLoginIdAndUsername(Long loginId, String username);

}