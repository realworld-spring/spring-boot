package real.world.domain.article.query;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.article.entity.Article;

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

    // TODO profile

    @QueryProjection
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
    }

}
