package real.world.domain.article.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import real.world.domain.article.entity.Article;
import real.world.domain.article.entity.Favorite;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.article.repository.FavoriteRepository;
import real.world.error.exception.AlreadyFavoriteException;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.FavoriteNotFoundException;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Nested
    class 좋아요는 {

        @Test
        void 정상_호출시_검증을_거쳐_좋아요를_저장한다() {
            // given
            final Long userId = JOHN.getId();
            final Long postId = 1L;
            final String slug = 게시물.getSlug();
            final Article article = 게시물.ID와_함께_생성(userId, postId);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));

            // when
            favoriteService.favorite(userId, slug);

            // then
            assertAll(() -> {
                verify(favoriteRepository).existsByUserIdAndArticleId(userId, postId);
                verify(favoriteRepository).save(any());
            });
        }

        @Test
        void 슬럭에_해당하는_아티클이_없다면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();

            // when & then
            assertThatThrownBy(() -> favoriteService.favorite(userId, slug))
                .isInstanceOf(ArticleNotFoundException.class);
        }

        @Test
        void 이미_좋아요가_존재하면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final Long postId = 1L;
            final String slug = 게시물.getSlug();
            final Article article = 게시물.ID와_함께_생성(userId, postId);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));
            given(favoriteRepository.existsByUserIdAndArticleId(userId, postId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> favoriteService.favorite(userId, slug))
                .isInstanceOf(AlreadyFavoriteException.class);
        }

    }

    @Nested
    class 좋아요취소는 {

        @Test
        void 정상_호출시_좋아요를_삭제한다() {
            // given
            final Long userId = JOHN.getId();
            final Long postId = 1L;
            final String slug = 게시물.getSlug();
            final Article article = 게시물.ID와_함께_생성(userId, postId);
            final Favorite favorite = new Favorite(userId, postId);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));
            given(favoriteRepository.findByUserIdAndArticleId(userId, postId)).willReturn(Optional.of(favorite));

            // when
            favoriteService.unfavorite(userId, slug);

            // then
            verify(favoriteRepository).delete(favorite);
        }

        @Test
        void 슬럭에_해당하는_아티클이_없다면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final String slug = 게시물.getSlug();

            // when & then
            assertThatThrownBy(() -> favoriteService.unfavorite(userId, slug))
                .isInstanceOf(ArticleNotFoundException.class);
        }

        @Test
        void 좋아요가_없으면_예외를_던진다() {
            // given
            final Long userId = JOHN.getId();
            final Long postId = 1L;
            final String slug = 게시물.getSlug();
            final Article article = 게시물.ID와_함께_생성(userId, postId);
            given(articleRepository.findBySlug(slug)).willReturn(Optional.of(article));

            // when & then
            assertThatThrownBy(() -> favoriteService.unfavorite(userId, slug))
                .isInstanceOf(FavoriteNotFoundException.class);
        }

    }

}
