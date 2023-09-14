package real.world.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.user.entity.User;

@Getter
@JsonRootName(value = "user")
@NoArgsConstructor
public class UserResponse {

    private String username;

    private String email;

    private String bio;

    private String image;

    private UserResponse(String username, String email, String bio, String image) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.image = image;
    }

    public static UserResponse of(User user) {
        return new UserResponse(user.getUsername(), user.getEmail(),
            user.getBio(), user.getImageUrl());
    }

}
