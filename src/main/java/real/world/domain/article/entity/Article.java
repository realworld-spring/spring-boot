package real.world.domain.article.entity;


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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import real.world.domain.article.service.SlugTranslator;
import real.world.error.exception.ArticleUnauthorizedException;

@Getter
@Entity(name = "articles")
@EntityListeners(AuditingEntityListener.class)
public class Article {

    @Id
    @Column(name = "article_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "article_tags", joinColumns = @JoinColumn(name = "article_id"))
    @Column(name = "name")
    private List<String> tags;

    protected Article() {
    }

    public Article(Long userId, String title, SlugTranslator translator, String description,
        String body, Collection<String> tags) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.body = body;
        this.tags = tags.stream().toList();
        setSlug(translator);
    }

    public Article(Long userId, String title, SlugTranslator translator, String description, String body) {
        this(userId, title, translator, description, body, Collections.emptyList());
    }

    public void update(Article article) {
        this.title = article.title;
        this.description = article.description;
        this.body = article.body;
        this.slug = article.slug;
    }

    public void verifyUserId(Long userId) {
        if (!userId.equals(this.userId)) {
            throw new ArticleUnauthorizedException();
        }
    }

    private void setSlug(SlugTranslator translator) {
        this.slug = translator.translate(title);
    }

}
