package real.world.domain.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserApiResponse {

    private UserDto user;

    public UserApiResponse(UserDto userDto) {
        this.user = userDto;
    }

}
