package real.world.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName(value = "user")
@NoArgsConstructor
public class UpdateRequest {

    private String email;

    private String username;

    private String password;

    private String bio;

    private String image;

}