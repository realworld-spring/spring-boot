package real.world.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserApiResponse {

    private UserResponse user;

    public UserApiResponse(UserResponse userResponse) {
        this.user = userResponse;
    }

}
