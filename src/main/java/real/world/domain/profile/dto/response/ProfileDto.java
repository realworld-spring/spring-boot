package real.world.domain.profile.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.profile.query.Profile;
import real.world.domain.user.entity.User;

@Getter
@NoArgsConstructor
public class ProfileDto {

    private String username;

    private String bio;

    private String image;

    private boolean following;

    private ProfileDto(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static ProfileDto of(User user, boolean following) {
        return new ProfileDto(user.getUsername(), user.getBio(), user.getImageUrl(), following);
    }

    public static ProfileDto of(Profile profile) {
        return new ProfileDto(
            profile.getUsername(),
            profile.getBio(),
            profile.getImage(),
            profile.isFollowing()
        );
    }

}