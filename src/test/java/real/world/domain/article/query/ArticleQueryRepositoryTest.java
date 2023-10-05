package real.world.domain.article.query;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.ArticleFixtures.게시물_2;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.follow.entity.Follow;
import real.world.domain.follow.repository.FollowRepository;
import real.world.domain.global.Page;
import real.world.domain.user.entity.User;
import real.world.domain.user.repository.UserRepository;
import real.world.support.QueryRepositoryTest;

@Import(ArticleQueryRepositoryImpl.class)
public class ArticleQueryRepositoryTest extends QueryRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ArticleQueryRepository articleQueryRepository;

    @Test
    void 아티클뷰를_ID로_단건_조회한다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article = articleRepository.save(게시물.태그와_함께_생성(john.getId(), tags));

        // when
        final Optional<ArticleView> articleView = articleQueryRepository.findById(john.getId(),
            article.getId());

        // then
        assertAll(() -> {
            assertThat(articleView).isPresent();
            assertArticleView(articleView.get(), article, tags);
        });
    }

    @Test
    void 아티클뷰를_슬럭으로_단건_조회한다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article = articleRepository.save(게시물.태그와_함께_생성(john.getId(), tags));

        // when
        final Optional<ArticleView> articleView = articleQueryRepository.findBySlug(JOHN.getId(),
            article.getSlug());

        // then
        assertAll(() -> {
            assertThat(articleView.isPresent()).isTrue();
            assertArticleView(articleView.get(), article, tags);
        });
    }

    @Test
    void 아티클뷰를_ID로_여러건_조회한다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));

        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article1 = articleRepository.save(게시물.태그와_함께_생성(alice.getId(), tags));
        final Article article2 = articleRepository.save(게시물_2.태그와_함께_생성(alice.getId(), tags));

        final Page page = new Page(0, 5);

        // when
        final List<ArticleView> articleView = articleQueryRepository.findByLoginId(john.getId(),
            page);

        // then
        assertAll(() -> {
            assertThat(articleView).hasSize(2);
            assertThat(articleView).satisfiesExactlyInAnyOrder(
                item -> assertArticleView(item, article1, tags),
                item -> assertArticleView(item, article2, tags)
            );
        });
    }

    @Test
    void 아티클뷰를_AUTHOR_조건으로_여러건_조회한다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));

        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article1 = articleRepository.save(게시물.태그와_함께_생성(alice.getId(), tags));
        final Article article2 = articleRepository.save(게시물_2.태그와_함께_생성(alice.getId(), tags));

        final Page page = new Page(0, 5);

        // when
        final List<ArticleView> articleView = articleQueryRepository.findRecent(john.getId(),
            page, null, alice.getUsername(), null);

        // then
        assertAll(() -> {
            assertThat(articleView).hasSize(2);
            assertThat(articleView).satisfiesExactlyInAnyOrder(
                item -> assertArticleView(item, article1, tags),
                item -> assertArticleView(item, article2, tags)
            );
        });
    }

    @Test
    void 아티클뷰를_TAG_조건으로_여러건_조회한다() {
        // given
        final User john = userRepository.save(JOHN.ID없이_생성());
        final User alice = userRepository.save(ALICE.ID없이_생성());
        followRepository.save(new Follow(alice, john));

        final Set<String> tags = Set.of("tag1", "tag2");
        final Article article1 = articleRepository.save(게시물.태그와_함께_생성(alice.getId(), tags));
        final Article article2 = articleRepository.save(게시물_2.태그와_함께_생성(alice.getId(), tags));

        final Page page = new Page(0, 5);

        // when
        final List<ArticleView> articleView = articleQueryRepository.findRecent(john.getId(),
            page, "tag1", null, null);

        // then
        assertAll(() -> {
            assertThat(articleView).hasSize(2);
            assertThat(articleView).satisfiesExactlyInAnyOrder(
                item -> assertArticleView(item, article1, tags),
                item -> assertArticleView(item, article2, tags)
            );
        });
    }

    private void assertArticleView(ArticleView articleView, Article article, Set<String> tags) {
        assertThat(articleView.getTitle()).isEqualTo(article.getTitle());
        assertThat(articleView.getDescription()).isEqualTo(article.getDescription());
        assertThat(articleView.getSlug()).isEqualTo(article.getSlug());
        assertThat(articleView.getBody()).isEqualTo(article.getBody());
        assertThat(articleView.getTagList()).containsExactlyInAnyOrderElementsOf(tags);
    }

}
