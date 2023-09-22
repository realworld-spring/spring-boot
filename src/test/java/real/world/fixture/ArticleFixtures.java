package real.world.fixture;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.test.util.ReflectionTestUtils;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.entity.Article;
import real.world.domain.article.query.ArticleView;
import real.world.domain.user.entity.User;

@Getter
public enum ArticleFixtures {

    게시물("title", "slug", "desc", "body", Collections.emptyList());

    private final String title;

    private final String slug;

    private final String description;

    private final String body;

    private final List<String> tags;

    ArticleFixtures(String title, String slug, String description, String body, List<String> tags) {
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.body = body;
        this.tags = tags;
    }

    public Article 생성(User user) {
        return new Article(
            user,
            this.title,
            this.slug,
            this.description,
            this.body,
            this.tags
        );
    }

    public Article 태그와_함께_생성(User user, Set<String> tags) {
        return new Article(
            user,
            this.title,
            this.slug,
            this.description,
            this.body,
            tags
        );
    }

    public ArticleView 뷰_생성(User user) {
        return new ArticleView(
            생성(user),
            false,
            0
        );
    }

    public UploadRequest 업로드를_한다() {
        final UploadRequest request = new UploadRequest();
        ReflectionTestUtils.setField(request, "title", this.title);
        ReflectionTestUtils.setField(request, "description", this.description);
        ReflectionTestUtils.setField(request, "body", this.body);
        ReflectionTestUtils.setField(request, "tags", Set.copyOf(this.tags));
        return request;
    }

    public UploadRequest 태그와_함께_업로드를_한다(Set<String> tags) {
        final UploadRequest request = new UploadRequest();
        ReflectionTestUtils.setField(request, "title", this.title);
        ReflectionTestUtils.setField(request, "description", this.description);
        ReflectionTestUtils.setField(request, "body", this.body);
        ReflectionTestUtils.setField(request, "tags", tags);
        return request;
    }

}
