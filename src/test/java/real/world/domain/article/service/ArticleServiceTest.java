package real.world.domain.article.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.Optional;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.entity.Article;
import real.world.domain.article.repository.ArticleRepository;
import real.world.domain.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

    private final UserRepository userRepository = BDDMockito.mock(UserRepository.class);

    private final ArticleRepository articleRepository = BDDMockito.mock(ArticleRepository.class);

    private final SlugTranslator slugTranslator = BDDMockito.mock(SlugTranslator.class);

    private final ArticleService articleService = new ArticleService(userRepository,
        articleRepository, slugTranslator);

    @Nested
    class 업로드는 {

        @Test
        void 정상_호출시_포스트를_저장한다() {
            // given
            final UploadRequest request = 게시물.업로드를_한다();
            given(userRepository.findById(JOHN.getId())).willReturn(Optional.of(JOHN.생성()));

            // when
            articleService.upload(JOHN.getId(), request);

            // then
            verify(articleRepository).save(any(Article.class));
        }

        @Test
        void 정상_호출시_번역기를_통해_슬럭을_생성한다() {
            // given
            final UploadRequest request = 게시물.업로드를_한다();
            given(userRepository.findById(JOHN.getId())).willReturn(Optional.of(JOHN.생성()));

            // when
            articleService.upload(JOHN.getId(), request);

            // then
            verify(slugTranslator).translate(request.getTitle());
        }

    }

}
