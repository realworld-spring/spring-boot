package real.world.domain.comment.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.comment.query.CommentView;
import real.world.domain.profile.dto.response.ProfileResponse;

@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body;

    private ProfileResponse author;

    private CommentResponse(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String body,
        ProfileResponse profileResponse) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.body = body;
        this.author = profileResponse;
    }

    public static CommentResponse of(CommentView commentView) {
        return new CommentResponse(
            commentView.getId(),
            commentView.getCreatedAt(),
            commentView.getUpdatedAt(),
            commentView.getBody(),
            ProfileResponse.of(commentView.getProfile())
        );
    }

}