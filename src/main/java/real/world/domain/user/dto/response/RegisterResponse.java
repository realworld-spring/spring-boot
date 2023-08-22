package real.world.domain.user.dto.response;

import lombok.Getter;
import real.world.domain.user.entity.User;

@Getter
public class RegisterResponse {

    private final String username;

    private final String email;

    private final String bio;

    private final String image;

    private RegisterResponse(String username, String email, String bio, String image) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.image = image;
    }

    public static RegisterResponse of(User user) {
        return new RegisterResponse(user.getUsername(), user.getEmail(),
            user.getBio(), user.getImageUrl());
    }

}
