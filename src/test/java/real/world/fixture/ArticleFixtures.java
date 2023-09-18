package real.world.fixture;

import jakarta.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.springframework.test.util.ReflectionTestUtils;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.entity.Article;
import real.world.domain.article.entity.Tag;
import real.world.domain.user.entity.User;

@Getter
public enum ArticleFixtures {

    태그_없는_게시물("title", "slug", "desc", "body", Collections.emptyList());

    private final String title;

    private final String slug;

    private final String description;

    private final String body;

    private final List<Tag> tagList;

    ArticleFixtures(String title, String slug, String description, String body, List<Tag> tagList) {
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
    }

    public Article 생성(User user) {
        return new Article(
            user,
            this.title,
            this.slug,
            this.description,
            this.body,
            this.tagList
        );
    }

    public UploadRequest 업로드를_한다() {
        final  UploadRequest request = new UploadRequest();
        ReflectionTestUtils.setField(request, "title", this.title);
        ReflectionTestUtils.setField(request, "description", this.description);
        ReflectionTestUtils.setField(request, "body", this.body);
        ReflectionTestUtils.setField(request, "tags", Set.copyOf(this.tagList));
        return request;
    }

}
