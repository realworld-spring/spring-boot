package real.world.domain.comment.query;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.comment.entity.Comment;
import real.world.domain.profile.query.Profile;

@Getter
@NoArgsConstructor
public class CommentView {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body;

    private Profile profile;

    public CommentView(Comment comment) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
    }

    public CommentView(Comment comment, Profile profile) {
        this.id = comment.getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.profile = profile;
    }

}