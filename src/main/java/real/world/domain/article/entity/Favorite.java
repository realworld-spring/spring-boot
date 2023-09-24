package real.world.domain.article.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.io.Serializable;
import lombok.Getter;

@Getter
@Entity(name = "favorites")
@IdClass(Favorite.FavoriteId.class)
public class Favorite {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "article_id")
    private Long articleId;

    protected Favorite() {
    }

    public Favorite(Long userId, Long articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    public static class FavoriteId implements Serializable {

        private final Long userId;

        private final Long articleId;

        public FavoriteId(Long userId, Long articleId) {
            this.userId = userId;
            this.articleId = articleId;
        }
    }

}
