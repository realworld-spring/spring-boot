package real.world.domain.profile.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileApiResponse {

    private ProfileResponse profile;

    public ProfileApiResponse(ProfileResponse profile) {
        this.profile = profile;
    }

}
