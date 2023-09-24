package real.world.domain.article.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.ArticleFixtures.게시물_2;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import org.junit.jupiter.api.Test;
import real.world.domain.article.service.SlugTranslator;
import real.world.error.exception.ArticleUnauthorizedException;
import real.world.support.StubSlugTranslator;

public class ArticleTest {

    private final SlugTranslator slugTranslator = new StubSlugTranslator();

    @Test
    void 생성_시_SlugTranslator를_통해_슬럭을_생성한다() {
        // given
        final Article article = new Article(JOHN.getId(), 게시물.getTitle(), slugTranslator,
            게시물.getDescription(), 게시물.getBody(), 게시물.getTags());

        // when & then
        assertThatThrownBy(() -> article.verifyUserId(ALICE.getId())).isInstanceOf(
            ArticleUnauthorizedException.class);
    }

    @Test
    void verifyUserId는_인자와_아티클의_userId가_다르면_예외를_던진다() {
        // given & when
        final Article article = new Article(JOHN.getId(), 게시물.getTitle(), slugTranslator,
            게시물.getDescription(), 게시물.getBody(), 게시물.getTags());

        // then
        assertThat(article.getSlug()).isEqualTo(slugTranslator.translate(게시물.getTitle()));
    }

    @Test
    void 수정_시_SlugTranslator를_통해_슬럭을_생성한다() {
        // given & when
        final Article article = new Article(JOHN.getId(), 게시물.getTitle(), slugTranslator,
            게시물.getDescription(), 게시물.getBody(), 게시물.getTags());
        final Article updateArticle = 게시물_2.생성(JOHN.getId());
        article.update(updateArticle);

        // then
        assertThat(article.getSlug()).isEqualTo(slugTranslator.translate(게시물_2.getTitle()));
    }

}
