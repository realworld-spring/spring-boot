package real.world.domain.comment.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.comment.dto.request.CommentRequest;
import real.world.domain.comment.dto.response.CommentResponse;
import real.world.domain.comment.entity.Comment;
import real.world.domain.comment.query.CommentView;
import real.world.domain.comment.repository.CommentRepository;
import real.world.domain.profile.query.Profile;
import real.world.domain.profile.query.ProfileQueryRepository;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.CommentNotFoundException;
import real.world.error.exception.UsernameNotExistException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ArticleRepository articleRepository;

    private final ProfileQueryRepository profileQueryRepository;

    public CommentService(CommentRepository commentRepository,
        ArticleRepository articleRepository, ProfileQueryRepository profileQueryRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.profileQueryRepository = profileQueryRepository;
    }

    @Transactional
    public CommentResponse upload(Long loginId, String slug, CommentRequest request) {
        final Article article = articleRepository.findBySlug(slug)
            .orElseThrow(ArticleNotFoundException::new);
        final Comment comment = new Comment(article.getId(), loginId, request.getBody());

        commentRepository.save(comment);

        final CommentView commentView = getCommentView(comment, loginId, article.getUserId());
        return CommentResponse.of(commentView);
    }

    @Transactional
    public void delete(Long loginId, Long id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(CommentNotFoundException::new);
        comment.verifyUserId(loginId);
        commentRepository.delete(comment);
    }

    private CommentView getCommentView(Comment comment, Long loginId, Long userId) {
        final CommentView commentView = new CommentView(comment);
        setProfile(commentView, loginId, userId);
        return commentView;
    }

    private void setProfile(CommentView commentView, Long loginId, Long userId) {
        final Profile profile = profileQueryRepository.findByLoginIdAndUserId(
            loginId, userId).orElseThrow(UsernameNotExistException::new);
        commentView.setProfile(profile);
    }

}