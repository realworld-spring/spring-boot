package real.world.domain.article.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.Getter;
import real.world.domain.user.entity.User;

@Getter
@Entity(name = "favorites")
@IdClass(Favorite.FavoriteId.class)
public class Favorite {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    protected Favorite() {
    }

    public Favorite(User user, Article article) {
        this.user = user;
        this.article = article;
    }

    static class FavoriteId implements Serializable {

        private final Long user;

        private final Long article;

        public FavoriteId(Long user, Long article) {
            this.user = user;
            this.article = article;
        }
    }

}
