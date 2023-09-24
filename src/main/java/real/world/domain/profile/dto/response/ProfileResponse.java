package real.world.domain.profile.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.profile.query.Profile;
import real.world.domain.user.entity.User;

@Getter
@JsonRootName(value = "profile")
@NoArgsConstructor
public class ProfileResponse {

    private String username;

    private String bio;

    private String image;

    private boolean following;

    private ProfileResponse(String username, String bio, String image, boolean following) {
        this.username = username;
        this.bio = bio;
        this.image = image;
        this.following = following;
    }

    public static ProfileResponse of(User user, boolean following) {
        return new ProfileResponse(user.getUsername(), user.getBio(), user.getImageUrl(), following);
    }

    public static ProfileResponse of(Profile profile) {
        return new ProfileResponse(
            profile.getUsername(),
            profile.getBio(),
            profile.getImage(),
            profile.isFollowing()
        );
    }

}