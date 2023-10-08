package real.world.domain.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentApiResponse {

    private CommentResponse comment;

    public CommentApiResponse(CommentResponse commentResponse) {
        this.comment = commentResponse;
    }

}