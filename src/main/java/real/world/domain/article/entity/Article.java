package real.world.domain.article.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import real.world.domain.user.entity.User;

@Getter
@Entity(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, unique = true, length = 60)
    private String slug;

    @Column(nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String body;

    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    protected Article() {
    }

    public Article(User user, String title, String slug, String description, String body) {
        this.user = user;
        this.title = title;
        this.slug = slug;
        this.description = description;
        this.body = body;
        this.favorites = Collections.emptyList();
    }

    public int getFavoritesCount() {
        return this.favorites.size();
    }

}
