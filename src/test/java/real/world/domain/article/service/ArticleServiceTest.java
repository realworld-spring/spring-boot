package real.world.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.UploadResponse;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    private final UserRepository userRepository = BDDMockito.mock(UserRepository.class);

    private final ArticleRepository articleRepository = BDDMockito.mock(ArticleRepository.class);

    private final SlugTranslator slugTranslator = new StubSlugTranslator();

    private final ArticleService articleService = new ArticleService(userRepository,
        articleRepository, slugTranslator);

    @Nested
    class 업로드는 {

        @Test
        void 정상_호출시_포스트를_저장하고_응답을_반환한다() {
            // given
            final UploadRequest request = 게시물.업로드를_한다();
            given(userRepository.findById(JOHN.getId())).willReturn(Optional.of(JOHN.생성()));

            // when
            final UploadResponse response = articleService.upload(JOHN.getId(), request);

            // then
            assertAll(() -> {
                verify(articleRepository).save(any(Article.class));
                assertThat(response.getTitle()).isEqualTo(request.getTitle());
                assertThat(response.getBody()).isEqualTo(request.getBody());
                assertThat(response.getDescription()).isEqualTo(request.getDescription());
            });
        }

        @Test
        void 정상_호출시_번역기를_통해_슬럭을_생성한다() {
            // given
            final UploadRequest request = 게시물.업로드를_한다();
            given(userRepository.findById(JOHN.getId())).willReturn(Optional.of(JOHN.생성()));

            // when
            final UploadResponse response = articleService.upload(JOHN.getId(), request);

            // then
            assertAll(() -> {
                assertThat(response.getSlug()).isEqualTo(
                    slugTranslator.translate(request.getTitle())
                );
            });
        }

        @Test
        void 태그_포함시_태그가_응답과_함께_반환된다() {
            // given
            final Set<String> tags = Set.of("tag1", "tag2");
            final UploadRequest request = 게시물.태그와_함께_업로드를_한다(tags);
            given(userRepository.findById(JOHN.getId())).willReturn(Optional.of(JOHN.생성()));

            // when
            final UploadResponse response = articleService.upload(JOHN.getId(), request);

            // then
            assertAll(() -> {
                assertThat(response.getTagList().size()).isEqualTo(request.getTags().size());
                assertThat(response.getTagList()).containsExactlyInAnyOrderElementsOf(request.getTags());
            });
        }

    }

}
