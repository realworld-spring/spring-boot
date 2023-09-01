package real.world.domain.user.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.user.entity.User;

@Getter
@JsonRootName(value = "user")
@NoArgsConstructor
public class LoginResponse {

    private String username;

    private String email;

    private String bio;

    private String image;

    public LoginResponse(String username, String email, String bio, String image) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.image = image;
    }

    public static LoginResponse of(User user) {
        return new LoginResponse(user.getUsername(), user.getEmail(),
            user.getBio(), user.getImageUrl());
    }

}
