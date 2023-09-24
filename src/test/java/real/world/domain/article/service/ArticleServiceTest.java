package real.world.domain.article.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.ArticleFixtures.게시물_2;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import real.world.domain.article.dto.request.ArticleUpdateRequest;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.error.exception.ArticleNotFoundException;
import real.world.support.StubSlugTranslator;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    private final ArticleRepository articleRepository = BDDMockito.mock(ArticleRepository.class);

    private final SlugTranslator slugTranslator = new StubSlugTranslator();

    private final ArticleService articleService = new ArticleService(articleRepository,
        slugTranslator);

    @Nested
    class 업로드는 {

        @Test
        void 정상_호출시_포스트를_저장한다() {
            // given
            final UploadRequest request = 게시물.업로드를_한다();

            // when
            articleService.upload(JOHN.getId(), request);

            // then
            verify(articleRepository).save(any(Article.class));
        }

    }

    @Nested
    class 게시물_수정은 {

        @Test
        void 정상_호출시_userId를_검증하고_포스트를_수정한다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();
            final ArticleUpdateRequest request = 게시물_2.수정을_한다();
            final Article article = BDDMockito.mock(Article.class);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));

            // when
            articleService.update(userId, slug, request);

            // then
            assertAll(() -> {
                verify(article).verifyUserId(anyLong());
                verify(article).update(any());
            });
        }

        @Test
        void 슬럭에_해당하는_아티클이_없다면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();
            final ArticleUpdateRequest request = 게시물_2.수정을_한다();

            // when & then
            assertThatThrownBy(() -> articleService.update(userId, slug, request))
                .isInstanceOf(ArticleNotFoundException.class);
        }

    }

    @Nested
    class 게시물_삭제는 {

        @Test
        void 정상_호출시_userId를_검증하고_포스트를_삭제한다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();
            final Article article = BDDMockito.mock(Article.class);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));

            // when
            articleService.delete(userId, slug);

            // then
            assertAll(() -> {
                verify(article).verifyUserId(anyLong());
                verify(articleRepository).delete(any());
            });
        }

        @Test
        void 슬럭에_해당하는_아티클이_없다면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();

            // when & then
            assertThatThrownBy(() -> articleService.delete(userId, slug))
                .isInstanceOf(ArticleNotFoundException.class);
        }

    }

}
