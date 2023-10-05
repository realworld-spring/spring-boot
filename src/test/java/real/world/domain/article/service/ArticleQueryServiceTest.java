package real.world.domain.article.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.ArticleFixtures.게시물_2;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.query.ArticleQueryRepository;
import real.world.domain.article.query.ArticleView;
import real.world.domain.global.Page;
import real.world.domain.profile.query.Profile;
import real.world.domain.profile.query.ProfileQueryRepository;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.UserIdNotExistException;

@ExtendWith(MockitoExtension.class)
public class ArticleQueryServiceTest {

    @Mock
    private ArticleQueryRepository articleQueryRepository;

    @Mock
    private ProfileQueryRepository profileQueryRepository;

    @InjectMocks
    private ArticleQueryService articleQueryService;

    @Nested
    class ID로_게시물_단건_조회는 {

        @Test
        void 정상_호출시_아티클_응답을_반환한다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");
            final ArticleView article = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);
            final Profile profile = JOHN.프로필_생성();

            given(articleQueryRepository.findById(loginId, 1L)).willReturn(Optional.of(article));
            given(profileQueryRepository.findByLoginIdAndUserId(loginId, loginId)).willReturn(Optional.of(profile));

            // when
            final ArticleResponse response = articleQueryService.getArticle(loginId, 1L);

            // then
            assertArticleResponse(response, article, profile);
        }

        @Test
        void ID에_일치하는_게시물이_없다면_예외를_던진다() {
            // given
            final Long loginId = JOHN.getId();
            final Long anyId = 1L;

            // when & then
            assertThatThrownBy(() -> articleQueryService.getArticle(loginId, anyId))
                .isInstanceOf(ArticleNotFoundException.class);
        }

        @Test
        void ID에_일치하는_유저가_없다면_예외를_던진다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");
            final ArticleView article = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);
            given(articleQueryRepository.findById(loginId, 1L)).willReturn(Optional.of(article));

            // when & then
            assertThatThrownBy(() -> articleQueryService.getArticle(loginId, 1L))
                .isInstanceOf(UserIdNotExistException.class);
        }

    }

    @Nested
    class 슬럭으로_게시물_단건_조회는 {

        @Test
        void 정상_호출시_아티클_응답을_반환한다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");
            final ArticleView article = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);
            final Profile profile = JOHN.프로필_생성();

            given(articleQueryRepository.findBySlug(loginId, article.getSlug())).willReturn(Optional.of(article));
            given(profileQueryRepository.findByLoginIdAndUserId(loginId, loginId)).willReturn(Optional.of(profile));

            // when
            final ArticleResponse response = articleQueryService.getArticle(loginId, article.getSlug());

            // then
            assertArticleResponse(response, article, profile);
        }

        @Test
        void 슬럭에_일치하는_게시물이_없다면_예외를_던진다() {
            // given
            final Long loginId = JOHN.getId();
            final String anySlug = "slug1";

            // when & then
            assertThatThrownBy(() -> articleQueryService.getArticle(loginId, anySlug))
                .isInstanceOf(ArticleNotFoundException.class);
        }

        @Test
        void ID에_일치하는_유저가_없다면_예외를_던진다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");
            final ArticleView article = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);

            given(articleQueryRepository.findBySlug(loginId, article.getSlug())).willReturn(Optional.of(article));

            // when & then
            assertThatThrownBy(() -> articleQueryService.getArticle(loginId, article.getSlug()))
                .isInstanceOf(UserIdNotExistException.class);
        }

    }

    @Nested
    class 팔로잉_피드_아티클_다수_조회는 {

        @Test
        void 정상_호출시_아티클_응답들을_반환한다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");

            final ArticleView article1 = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);
            final ArticleView article2 = 게시물_2.프로필_없이_뷰_생성(ALICE.getId(), tags);

            final Profile profile1 = JOHN.프로필_생성();
            final Profile profile2 = ALICE.프로필_생성();

            final Page page = new Page(0, 5);
            given(articleQueryRepository.findByLoginId(loginId, page)).willReturn(List.of(article1, article2));
            given(profileQueryRepository.findByLoginIdAndIds(eq(loginId), anySet()))
                .willReturn(Map.of(JOHN.getId(), profile1, ALICE.getId(), profile2));

            // when
            final List<ArticleResponse> responses = articleQueryService.getArticles(loginId, page);

            // then
            assertThat(responses).satisfiesExactlyInAnyOrder(
                response -> assertArticleResponse(response, article1, profile1),
                response -> assertArticleResponse(response, article2, profile2)
            );
        }

    }

    @Nested
    class 피드_조건_아티클_다수_조회는 {

        @Test
        void 정상_호출시_아티클_응답들을_반환한다() {
            // given
            final Long loginId = JOHN.getId();

            final Set<String> tags = Set.of("tag1", "tag2");

            final ArticleView article1 = 게시물.프로필_없이_뷰_생성(JOHN.getId(), tags);
            final ArticleView article2 = 게시물_2.프로필_없이_뷰_생성(ALICE.getId(), tags);

            final Profile profile1 = JOHN.프로필_생성();
            final Profile profile2 = ALICE.프로필_생성();

            final Page page = new Page(0, 5);
            given(articleQueryRepository.findRecent(eq(loginId), eq(page), any(), any(), any()))
                .willReturn(List.of(article1, article2));
            given(profileQueryRepository.findByLoginIdAndIds(eq(loginId), anySet()))
                .willReturn(Map.of(JOHN.getId(), profile1, ALICE.getId(), profile2));

            // when
            final List<ArticleResponse> responses = articleQueryService
                .getRecent(loginId, page, null, null, null);

            // then
            assertThat(responses).satisfiesExactlyInAnyOrder(
                response -> assertArticleResponse(response, article1, profile1),
                response -> assertArticleResponse(response, article2, profile2)
            );
        }

    }

    private void assertArticleResponse(ArticleResponse response, ArticleView articleView, Profile profile) {
        assertAll(() -> {
            assertThat(response.getTitle()).isEqualTo(articleView.getTitle());
            assertThat(response.getDescription()).isEqualTo(articleView.getDescription());
            assertThat(response.getSlug()).isEqualTo(articleView.getSlug());
            assertThat(response.getBody()).isEqualTo(articleView.getBody());
            assertThat(response.getTagList()).containsExactlyInAnyOrderElementsOf(articleView.getTagList());

            assertThat(response.getAuthor().getUsername()).isEqualTo(profile.getUsername());
            assertThat(response.getAuthor().getBio()).isEqualTo(profile.getBio());
            assertThat(response.getAuthor().getImage()).isEqualTo(profile.getImage());
        });
    }

}
