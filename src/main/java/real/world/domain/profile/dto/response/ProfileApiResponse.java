package real.world.domain.profile.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileApiResponse {

    private ProfileDto profile;

    public ProfileApiResponse(ProfileDto profile) {
        this.profile = profile;
    }

}
