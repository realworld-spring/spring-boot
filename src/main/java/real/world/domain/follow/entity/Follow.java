package real.world.domain.follow.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import real.world.domain.user.entity.User;

@Getter
@Entity(name = "follows")
@IdClass(Follow.FollowId.class)
public class Follow {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    protected Follow() {
    }

    public Follow(User user, User follower) {
        this.user = user;
        this.follower = follower;
    }

    static public class FollowId implements Serializable {
        Long user;
        Long follower;

    }

}
