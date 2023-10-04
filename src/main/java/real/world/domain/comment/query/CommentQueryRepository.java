package real.world.domain.comment.query;

import java.util.List;

public interface CommentQueryRepository {

    List<CommentView> findBySlug(String slug);

}