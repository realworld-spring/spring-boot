package real.world.domain.comment.dto.response;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MultiCommentsApiResponse {

    private List<CommentResponse> comments;

    public MultiCommentsApiResponse(List<CommentResponse> comments) {
        this.comments = comments;
    }

}