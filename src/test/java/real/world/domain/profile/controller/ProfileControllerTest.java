package real.world.domain.profile.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static real.world.fixture.UserFixtures.ALICE;
import static real.world.fixture.UserFixtures.JOHN;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import real.world.config.WebMvcConfig;
import real.world.domain.follow.service.FollowService;
import real.world.domain.profile.dto.response.ProfileDto;
import real.world.domain.profile.service.ProfileQueryService;
import real.world.domain.user.entity.User;
import real.world.support.TestSecurityConfig;
import real.world.support.WithMockUserId;
import real.world.support.WithMockUserIdFactory;

@WebMvcTest(controllers = {ProfileController.class})
@Import({TestSecurityConfig.class, WebMvcConfig.class, WithMockUserIdFactory.class})
class ProfileControllerTest {

    @MockBean
    private FollowService followService;

    @MockBean
    private ProfileQueryService profileQueryService;

    @Autowired
    private MockMvc mockmvc;

    @Nested
    class 팔로우_요청 {

        @Test
        @WithMockUserId(user = ALICE)
        void 상태코드_200로_성공() throws Exception {
            // given
            final User user = JOHN.생성();
            given(followService.follow(anyLong(), anyString())).willReturn(
                ProfileDto.of(user, true));

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/profiles/"+ user.getUsername() +"/follow"));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(followService).follow(anyLong(), anyString());
        }

    }

    @Nested
    class 팔로우_취소_요청 {

        @Test
        @WithMockUserId(user = ALICE)
        void 상태코드_200로_성공() throws Exception {
            // given
            final User user = JOHN.생성();
            given(followService.unfollow(anyLong(), anyString())).willReturn(
                ProfileDto.of(user, false));

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/profiles/"+ user.getUsername() +"/unfollow"));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(followService).unfollow(anyLong(), anyString());
        }

    }

    @Nested
    class 프로필_요청 {

        @Test
        @WithMockUserId(user = ALICE)
        void 상태코드_200로_성공() throws Exception {
            // given
            final User user = JOHN.생성();
            given(profileQueryService.getProfile(anyLong(), anyString())).willReturn(
                ProfileDto.of(user, false));

            // when
            final ResultActions resultActions = mockmvc.perform(
                get("/profiles/"+ user.getUsername()));

            // then
            resultActions.andExpect(status().isOk())
                .andDo(print());
            verify(profileQueryService).getProfile(anyLong(), anyString());
        }

    }

}