package real.world.domain.article.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "tag_articles")
@IdClass(TagArticle.TagArticleId.class)
public class TagArticle {

    @Id
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Id
    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;

    protected TagArticle() {
    }

    public TagArticle(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class TagArticleId implements Serializable {

        private Long article;

        private Long tag;

        public TagArticleId(Long article, Long tag) {
            this.article = article;
            this.tag = tag;
        }
    }

}
