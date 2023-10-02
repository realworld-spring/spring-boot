package real.world.domain.profile.query;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface ProfileQueryRepository {

    Optional<Profile> findByLoginIdAndUsername(Long loginId, String username);

    Optional<Profile> findByLoginIdAndUserId(Long loginId, Long userId);

    Map<Long, Profile> findByLoginIdAndIds(Long loginId, Set<Long> ids);

}