package real.world.domain.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SingleCommentApiResponse {

    private CommentResponse comment;

    public SingleCommentApiResponse(CommentResponse commentResponse) {
        this.comment = commentResponse;
    }

}