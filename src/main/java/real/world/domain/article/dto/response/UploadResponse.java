package real.world.domain.article.dto.response;

import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import real.world.domain.article.entity.Article;
import real.world.domain.user.dto.ProfileDto;

@Getter
@JsonRootName(value = "article")
@NoArgsConstructor
public class UploadResponse {

    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean favorited;
    private int favoritesCount;

    private ProfileDto author;

    private UploadResponse(String slug, String title, String description, String body,
        List<String> tagList, LocalDateTime createdAt, LocalDateTime updatedAt, boolean favorited,
        int favoritesCount, ProfileDto author) {
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

    public static UploadResponse of(Article article) {
        return new UploadResponse(
            article.getSlug(), article.getTitle(), article.getDescription(),
            article.getBody(), article.getTags(), article.getCreatedAt(),
            article.getUpdatedAt(), false, article.getFavoritesCount(),
            ProfileDto.of(article.getUser(), false)
        );
    }

}
