package real.world.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.article.query.ArticleView;
import real.world.domain.profile.query.Profile;

@Getter
@JsonRootName(value = "article")
@NoArgsConstructor
public class ArticleResponse {

    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean favorited;
    private long favoritesCount;

    private Profile author;

    private ArticleResponse(String slug, String title, String description, String body,
        List<String> tagList, LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited,
        long favoritesCount, Profile author) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tagList = tagList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.favorited = favorited;
        this.favoritesCount = favoritesCount;
        this.author = author;
    }

    public static ArticleResponse of(ArticleView article) {
        return new ArticleResponse(
            article.getSlug(), article.getTitle(), article.getDescription(),
            article.getBody(), article.getTagList(), article.getCreatedAt(),
            article.getUpdatedAt(), article.isFavorited(), article.getFavoritesCount(),
            null // TODO
        );
    }

}
