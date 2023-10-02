package real.world.domain.article.query;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.article.entity.Article;
import real.world.domain.profile.query.Profile;

@Getter
@NoArgsConstructor
public class ArticleView {

    private String title;

    private String slug;

    private String description;

    private String body;

    private List<String> tagList;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean favorited;

    private long favoritesCount;

    private Long userId;

    private Profile profile;

    public ArticleView(Article article, boolean favorited, long favoritesCount) {
        this.title = article.getTitle();
        this.slug = article.getSlug();
        this.description = article.getDescription();
        this.body = article.getBody();
        this.tagList = article.getTags();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.userId = article.getUserId();
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

}
