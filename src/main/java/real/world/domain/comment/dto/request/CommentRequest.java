package real.world.domain.comment.dto.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonRootName(value = "comment")
@NoArgsConstructor
public class CommentRequest {

    @NotBlank
    String body;

}