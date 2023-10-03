package real.world.domain.profile.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.user.entity.User;

@Getter
@NoArgsConstructor
public class Profile {

    private Long id;

    private String username;

    private String bio;

    private String image;

    private boolean following;

    private Profile(Long id, String username, String bio, String image, boolean following) {
        this.id = id;
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public Profile(User user, boolean following) {
        this(user.getId(), user.getUsername(), user.getBio(), user.getImageUrl(), following);
    }

    public static Profile of(User user, boolean following) {
        return new Profile(user.getId(), user.getUsername(), user.getBio(), user.getImageUrl(),
            following);
    }

}
