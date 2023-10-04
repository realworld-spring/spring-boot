package real.world.domain.comment.repository;

import org.springframework.data.repository.Repository;
import real.world.domain.comment.entity.Comment;

public interface CommentRepository extends Repository<Comment, Long> {

    Comment save(Comment comment);

}