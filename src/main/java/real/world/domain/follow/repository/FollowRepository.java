package real.world.domain.follow.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.entity.Follow.FollowId;

public interface FollowRepository extends Repository<Follow, FollowId> {

    Follow save(Follow follow);

    boolean existsByUserIdAndFollowerId(Long userId, Long followerId);

    Optional<Follow> findByUserIdAndFollowerId(Long userId, Long followerId);

}