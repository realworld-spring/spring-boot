package real.world.domain.article.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import real.world.domain.article.service.SlugTranslator;
import real.world.domain.user.entity.User;
import real.world.error.exception.ArticleUnauthorizedException;

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

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Favorite> favorites;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "name")
    private List<String> tags;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    protected Article() {
    }

    public Article(User user, String title, SlugTranslator translator, String description,
        String body, Collection<String> tags) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.body = body;
        this.favorites = Collections.emptyList();
        this.tags = tags.stream().toList();
        setSlug(translator);
    }

    public Article(String title, SlugTranslator translator, String description, String body) {
        this(null, title, translator, description, body, Collections.emptyList());
    }

    public void update(Long userId, Article article) {
        verifyUserId(userId);
        this.title = article.title;
        this.description = article.description;
        this.body = article.body;
        this.slug = article.slug;
    }

    public void verifyUserId(Long userId) {
        if (!userId.equals(this.user.getId())) {
            throw new ArticleUnauthorizedException();
        }
    }

    private void setSlug(SlugTranslator translator) {
        this.slug = translator.translate(title);
    }

}
