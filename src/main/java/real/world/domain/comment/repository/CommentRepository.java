package real.world.domain.comment.repository;

import java.util.Optional;
import org.springframework.data.repository.Repository;
import real.world.domain.comment.entity.Comment;

public interface CommentRepository extends Repository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Comment save(Comment comment);

    void delete(Comment comment);

}