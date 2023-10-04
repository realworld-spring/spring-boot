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

    private Long userId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String body;

    private Profile profile;

    public CommentView(Comment comment) {
        this.id = comment.getId();
        this.userId = comment.getUserId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
    }

    public CommentView(Comment comment, Profile profile) {
        this.id = comment.getId();
        this.userId = comment.getUserId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
        this.body = comment.getBody();
        this.profile = profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}