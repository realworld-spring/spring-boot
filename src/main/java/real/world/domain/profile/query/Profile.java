package real.world.domain.profile.query;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.user.entity.User;

@Getter
@NoArgsConstructor
public class Profile {

    private String username;

    private String bio;

    private String image;

    private boolean following;

    private Profile(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    @QueryProjection
    public Profile(User user, boolean following) {
        this(user.getUsername(), user.getBio(), user.getImageUrl(), following);
    }

    public static Profile of(User user, boolean following) {
        return new Profile(user.getUsername(), user.getBio(), user.getImageUrl(), following);
    }

}
