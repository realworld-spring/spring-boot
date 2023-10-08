package real.world.domain.comment.dto.response;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MultipleCommentApiResponse {

    private List<CommentResponse> comments;

    public MultipleCommentApiResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

}