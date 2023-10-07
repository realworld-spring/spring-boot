package real.world.domain.article.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import real.world.config.WebMvcConfig;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.query.ArticleView;
import real.world.domain.article.service.ArticleQueryService;
import real.world.domain.article.service.FavoriteService;
import real.world.error.exception.AlreadyFavoriteException;
import real.world.error.exception.ArticleNotFoundException;
import real.world.error.exception.FavoriteNotFoundException;
import real.world.support.TestSecurityConfig;
import real.world.support.WithMockUserId;
import real.world.support.WithMockUserIdFactory;

@WebMvcTest(controllers = {FavoriteController.class})
@Import({TestSecurityConfig.class, WebMvcConfig.class, WithMockUserIdFactory.class})
public class FavoriteControllerTest {

    @MockBean
    private FavoriteService favoriteService;

    @MockBean
    private ArticleQueryService articleQueryService;

    @Autowired
    private MockMvc mockmvc;

    @Nested
    class 좋아요 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final long 게시물_ID = 1L;
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final String slug = article.getSlug();

            given(favoriteService.favorite(JOHN.getId(), slug)).willReturn(게시물_ID);
            given(articleQueryService.getArticle(JOHN.getId(), 게시물_ID))
                .willReturn(ArticleResponse.of(article));

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            Assertions.assertAll(() -> {
                verify(favoriteService).favorite(eq(JOHN.getId()), eq(slug));
                verify(articleQueryService).getArticle(eq(JOHN.getId()), eq(게시물_ID));
            });
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 슬럭에_해당하는_아티클이_없다면_상태코드422로_실패() throws Exception {
            // given
            final String slug = 게시물.getSlug();
            given(favoriteService.favorite(JOHN.getId(), slug)).willThrow(new ArticleNotFoundException());

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 이미_좋아요를_눌렀다면_상태코드422로_실패() throws Exception {
            // given
            final String slug = 게시물.getSlug();
            given(favoriteService.favorite(JOHN.getId(), slug)).willThrow(new AlreadyFavoriteException());

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

    }

    @Nested
    class 좋아요취소 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final long 게시물_ID = 1L;
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final String slug = article.getSlug();

            given(favoriteService.unfavorite(JOHN.getId(), slug)).willReturn(게시물_ID);
            given(articleQueryService.getArticle(JOHN.getId(), 게시물_ID))
                .willReturn(ArticleResponse.of(article));

            // when
            final ResultActions resultActions = mockmvc.perform(
                delete("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            Assertions.assertAll(() -> {
                verify(favoriteService).unfavorite(eq(JOHN.getId()), eq(slug));
                verify(articleQueryService).getArticle(eq(JOHN.getId()), eq(게시물_ID));
            });
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 슬럭에_해당하는_아티클이_없다면_상태코드422로_실패() throws Exception {
            // given
            final String slug = 게시물.getSlug();
            given(favoriteService.unfavorite(JOHN.getId(), slug)).willThrow(new ArticleNotFoundException());

            // when
            final ResultActions resultActions = mockmvc.perform(
                delete("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 좋아요가_누른적이_없다면_상태코드422로_실패() throws Exception {
            // given
            final String slug = 게시물.getSlug();
            given(favoriteService.unfavorite(JOHN.getId(), slug)).willThrow(new FavoriteNotFoundException());

            // when
            final ResultActions resultActions = mockmvc.perform(
                delete("/articles/{slug}/favorite", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

    }

}
