package real.world.domain.article.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import real.world.config.JpaConfig;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.fixture.ArticleFixtures;
import real.world.fixture.UserFixtures;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaConfig.class, ArticleQueryRepositoryImpl.class})
public class ArticleQueryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleQueryRepository articleQueryRepository;

    @Test
    void 아티클뷰를_ID로_단건_조회한다() {
        // given
        final User user = UserFixtures.JOHN.생성();
        userRepository.save(user);
        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article = ArticleFixtures.게시물.태그와_함께_생성(user.getId(), tags);
        articleRepository.save(article);

        // when
        final Optional<ArticleView> articleView = articleQueryRepository.findById(user.getId(),
            article.getId());

        // then
        assertAll(() -> {
            assertThat(articleView.isPresent()).isTrue();
            final ArticleView result = articleView.get();
            assertThat(result.getTitle()).isEqualTo(article.getTitle());
            assertThat(result.getDescription()).isEqualTo(article.getDescription());
            assertThat(result.getSlug()).isEqualTo(article.getSlug());
            assertThat(result.getBody()).isEqualTo(article.getBody());
            assertThat(result.getTagList()).containsExactlyInAnyOrderElementsOf(tags);
        });
    }
    
    @Test
    void 아티클뷰를_슬럭으로_단건_조회한다() {
        // given
        final User user = UserFixtures.JOHN.생성();
        userRepository.save(user);
        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article = ArticleFixtures.게시물.태그와_함께_생성(user.getId(), tags);
        articleRepository.save(article);

        // when
        final Optional<ArticleView> articleView = articleQueryRepository.findBySlug(user.getId(),
            article.getSlug());

        // then
        assertAll(() -> {
            assertThat(articleView.isPresent()).isTrue();
            final ArticleView result = articleView.get();
            assertThat(result.getTitle()).isEqualTo(article.getTitle());
            assertThat(result.getDescription()).isEqualTo(article.getDescription());
            assertThat(result.getSlug()).isEqualTo(article.getSlug());
            assertThat(result.getBody()).isEqualTo(article.getBody());
            assertThat(result.getTagList()).containsExactlyInAnyOrderElementsOf(tags);
        });
    }

}
