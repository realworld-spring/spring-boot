package real.world.domain.article.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static real.world.fixture.ArticleFixtures.게시물;
import static real.world.fixture.UserFixtures.JOHN;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
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
import real.world.domain.article.dto.request.ArticleUpdateRequest;
import real.world.domain.article.dto.request.UploadRequest;
import real.world.domain.article.dto.response.ArticleResponse;
import real.world.domain.article.query.ArticleView;
import real.world.domain.article.service.ArticleQueryService;
import real.world.domain.article.service.ArticleService;
import real.world.error.exception.ArticleNotFoundException;
import real.world.support.TestSecurityConfig;
import real.world.support.WithMockUserId;
import real.world.support.WithMockUserIdFactory;

@WebMvcTest(controllers = {ArticleController.class})
@Import({TestSecurityConfig.class, WebMvcConfig.class, WithMockUserIdFactory.class})
public class ArticleControllerTest {

    @MockBean
    private ArticleService articleService;

    @MockBean
    private ArticleQueryService articleQueryService;

    @Autowired
    private MockMvc mockmvc;

    private ObjectMapper objectMapper;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        objectMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
    }

    @Nested
    class 게시물_업로드 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final UploadRequest request = 게시물.업로드를_한다();
            final long 게시물_ID = 1L;

            given(articleService.upload(eq(JOHN.getId()), any())).willReturn(게시물_ID);
            given(articleQueryService.getArticle(JOHN.getId(), 게시물_ID))
                .willReturn(ArticleResponse.of(article));

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/articles").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(print());
            verify(articleService).upload(eq(JOHN.getId()), any());
        }

    }

    @Nested
    class 게시물_조회 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final String slug = article.getSlug();
            final long 게시물_ID = 1L;

            given(articleQueryService.getArticle(JOHN.getId(), 게시물_ID))
                .willReturn(ArticleResponse.of(article));

            // when
            final ResultActions resultActions = mockmvc.perform(
                get("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(articleQueryService).getArticle(eq(JOHN.getId()), eq(slug));
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 슬럭에_해당하는_아티클이_없다면_상태코드422로_실패() throws Exception {
            // given
            final String slug = 게시물.getSlug();
            given(articleQueryService.getArticle(eq(JOHN.getId()), eq(slug))).willThrow(
                new ArticleNotFoundException()
            );

            // when
            final ResultActions resultActions = mockmvc.perform(
                get("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

    }

    @Nested
    class 게시물_수정 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final ArticleUpdateRequest request = 게시물.수정을_한다();
            final String slug = article.getSlug();
            final long 게시물_ID = 1L;

            given(articleService.update(eq(JOHN.getId()), eq(slug), any())).willReturn(게시물_ID);
            given(articleQueryService.getArticle(JOHN.getId(), 게시물_ID))
                .willReturn(ArticleResponse.of(article));

            // when
            final ResultActions resultActions = mockmvc.perform(
                put("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(articleService).update(eq(JOHN.getId()), eq(slug), any());
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 슬럭에_해당하는_아티클이_없다면_상태코드422로_실패() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final ArticleUpdateRequest request = 게시물.수정을_한다();
            final String slug = article.getSlug();

            given(articleService.update(eq(JOHN.getId()), eq(slug), any())).willThrow(
                new ArticleNotFoundException()
            );

            // when
            final ResultActions resultActions = mockmvc.perform(
                put("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

    }

    @Nested
    class 게시물_삭제 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드200_으로_성공() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final String slug = article.getSlug();

            // when
            final ResultActions resultActions = mockmvc.perform(
                delete("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(articleService).delete(eq(JOHN.getId()), eq(slug));
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 슬럭에_해당하는_아티클이_없다면_상태코드422로_실패() throws Exception {
            // given
            final ArticleView article = 게시물.뷰_생성(JOHN.생성());
            final String slug = article.getSlug();

            willThrow(new ArticleNotFoundException()).given(articleService)
                .delete(eq(JOHN.getId()), eq(slug));

            // when
            final ResultActions resultActions = mockmvc.perform(
                delete("/articles/{slug}", slug).contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
        }

    }

}
