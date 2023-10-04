package real.world.domain.comment.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import real.world.domain.comment.dto.response.CommentResponse;
import real.world.domain.comment.query.CommentQueryRepository;
import real.world.domain.comment.query.CommentView;
import real.world.domain.profile.query.Profile;
import real.world.domain.profile.query.ProfileQueryRepository;

@Service
public class CommentQueryService {

    final private CommentQueryRepository commentQueryRepository;

    final private ProfileQueryRepository profileQueryRepository;

    public CommentQueryService(CommentQueryRepository commentQueryRepository,
        ProfileQueryRepository profileQueryRepository) {
        this.commentQueryRepository = commentQueryRepository;
        this.profileQueryRepository = profileQueryRepository;
    }

    public List<CommentResponse> getComments(Long loginId, String slug) {
        List<CommentView> commentViews = commentQueryRepository.findBySlug(slug);
        setProfile(loginId, commentViews);

        return commentViews.stream().map(CommentResponse::of).toList();
    }

    private void setProfile(Long loginId, List<CommentView> commentViews) {
        final Set<Long> ids = commentViews.stream().map(CommentView::getUserId)
            .collect(Collectors.toSet());
        final Map<Long, Profile> profiles = profileQueryRepository.findByLoginIdAndIds(loginId,
            ids);
        commentViews.forEach((cv) -> cv.setProfile(profiles.get(cv.getUserId())));
    }

}