package real.world.domain.user.controller;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static real.world.fixture.UserFixtures.JOHN;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import real.world.config.WebMvcConfig;
import real.world.domain.user.dto.request.RegisterRequest;
import real.world.domain.user.dto.request.UpdateRequest;
import real.world.domain.user.dto.response.UserResponse;
import real.world.domain.user.entity.User;
import real.world.domain.user.service.UserService;
import real.world.error.exception.UserIdNotExistException;
import real.world.error.exception.UsernameAlreadyExistsException;
import real.world.security.support.JwtUtil;
import real.world.support.TestSecurityConfig;
import real.world.support.WithMockUserId;
import real.world.support.WithMockUserIdFactory;

@WebMvcTest(controllers = {UserController.class})
@Import({TestSecurityConfig.class, WebMvcConfig.class, WithMockUserIdFactory.class})
@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class})
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockmvc;

    private ObjectMapper objectMapper;

    @PostConstruct
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    }

    @Nested
    class 회원가입 {

        @Test
        void 상태코드_201로_성공() throws Exception {
            // given
            final User user = JOHN.생성();
            final RegisterRequest request = JOHN.회원가입을_한다();
            given(userService.register(any())).willReturn(UserResponse.of(user));

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/api/users").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(document("register"))
                .andDo(print());
            verify(userService).register(any());
        }

        @Test
        void 유저네임_중복시_상태코드_422로_실패() throws Exception {
            // given
            final RegisterRequest request = JOHN.회원가입을_한다();
            given(userService.register(any())).willThrow(new UsernameAlreadyExistsException());

            // when
            final ResultActions resultActions = mockmvc.perform(
                post("/api/users").contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
            verify(userService).register(any());
        }
    }

    @Nested
    class 유저조회 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드_201로_성공() throws Exception {
            // given
            final User user = JOHN.생성();
            final Long id = JOHN.getId();
            given(userService.getUser(id)).willReturn(UserResponse.of(user));

            // when
            final ResultActions resultActions = mockmvc.perform(get("/api/user"));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(document("getUser"))
                .andDo(print());
            verify(userService).getUser(any());
        }

        @Test
        @WithMockUserId(user = JOHN)
        void 유저ID가_존재하지_않는다면_상태코드_422로_실패() throws Exception {
            // given
            given(userService.getUser(any())).willThrow(new UserIdNotExistException());

            // when
            final ResultActions resultActions = mockmvc.perform(get("/api/user"));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
            verify(userService).getUser(any());
        }

    }

    @Nested
    class 유저수정 {

        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드_200으로_성공() throws Exception {
            // given
            given(userService.update(anyLong(), any(UpdateRequest.class))).willReturn(new UserResponse());

            // when
            final ResultActions resultActions = mockmvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateRequest())));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(document("update"))
                .andDo(print());
            verify(userService).update(anyLong(), any(UpdateRequest.class));
        }

        @Test
        @WithMockUserId(user = JOHN)
        void UserID가_존재하지_않는다면_상태_422로_실패() throws Exception {
            // given
            given(userService.update(anyLong(), any(UpdateRequest.class))).willThrow(new UserIdNotExistException());

            // when
            final ResultActions resultActions = mockmvc.perform(put("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UpdateRequest())));

            // then
            resultActions.andExpect(status().isUnprocessableEntity())
                .andDo(print());
            verify(userService).update(anyLong(), any(UpdateRequest.class));
        }

    }

    @Nested
    class 프로필조회 {

        // TODO 일단 테스트 API로 작성하였으니 추후에 구현후 수정할 것
        @Test
        @WithMockUserId(user = JOHN)
        void 상태코드_200으로_성공() throws Exception {
            // given & when
            final ResultActions resultActions = mockmvc.perform(
                get("/api/profiles"));

            // then
            resultActions.andExpect(status().isCreated())
                .andDo(document("profile"))
                .andDo(print());
        }

    }

}
