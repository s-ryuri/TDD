package spring.tdd.api;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.tdd.GlobalExceptionHandler;
import spring.tdd.application.MemberShipErrorResult;
import spring.tdd.application.MemberShipException;
import spring.tdd.application.MemberShipResponse;
import spring.tdd.application.MemberShipService;
import spring.tdd.domain.MemberShipType;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MemberShipControllerTest {

    @InjectMocks
    private MemberShipController memberShipController;

    @Mock
    private MemberShipService memberShipService;

    private MockMvc mockMvc;
    private Gson gson;

    @BeforeEach
    void init() {
        //beforEach를 쓰면 null Test가 필요없음
        gson = new Gson();
        mockMvc = MockMvcBuilders.standaloneSetup(memberShipController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    void 멤버십등록실패_사용자식별값이헤더에없음() throws Exception {
        final String url = "/api/v1/memberShip";

        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .content(gson.toJson(memberShipRequest(1000, MemberShipType.NAVER)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());

    }

    private MemberShipRequest memberShipRequest(Integer price, MemberShipType memberShipType) {
        return MemberShipRequest.builder()
                                .point(price)
                                .memberShipType(memberShipType)
                                .build();
    }

    @ParameterizedTest
    @MethodSource("invalidMembershipAddParameter")
    void 멤버십등록실패_잘못된파라미터(final Integer point, final MemberShipType memberShipType) throws Exception {

        final String url = "/api/v1/memberShip";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(point, memberShipType)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    private static Stream<Arguments> invalidMembershipAddParameter() {
        return Stream.of(
            Arguments.of(null, MemberShipType.KAKAO),
            Arguments.of(-1, MemberShipType.NAVER),
            Arguments.of(10000, null)
        );
    }

    @Test
    void 멤버십등록실패_MemberService에서ErrorThrow() throws Exception {
        //given
        final String url = "/api/v1/memberShip";
        doThrow(new MemberShipException(MemberShipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER))
            .when(memberShipService)
            .registerMemberShip("12345", MemberShipType.KAKAO, 1000);

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(10000, MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버십등록성공() throws Exception {

        final String url = "/api/v1/memberShip";
        final MemberShipResponse memberShipResponse = MemberShipResponse.builder()
                                                                        .id(-1L)
                                                                        .memberShipType(MemberShipType.KAKAO)
                                                                        .build();

        doReturn(memberShipResponse).when(memberShipService)
                                    .registerMemberShip("12345", MemberShipType.KAKAO, 10000);

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(10000, MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isCreated());

        final MemberShipResponse response = gson.fromJson(resultActions.andReturn()
                                                                       .getResponse()
                                                                       .getContentAsString(StandardCharsets.UTF_8),
                                                          MemberShipResponse.class);

        assertThat(response.getMemberShipType()).isEqualTo(MemberShipType.KAKAO);
        assertThat(response.getId()).isNotNull();
    }

    @Test
    void 멤버십목록조회실패_사용자식별값이헤더에없음() throws Exception {
        final String url = "/api/v1/memberShip";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버십목록조회성공() throws Exception {

        // given
        final String url = "/api/v1/memberShip";
        doReturn(Arrays.asList(
            MemberShipDetailResponse.builder().build(),
            MemberShipDetailResponse.builder().build(),
            MemberShipDetailResponse.builder().build()
        )).when(memberShipService)
          .getMemberShipList("12345");

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
        );

        resultActions.andExpect(status().isOk());

    }

    @Test
    void 멤버십삭제실패_사용자식별값이헤더에없음() throws Exception {
        final String url = "/api/v1/memberShip/-1";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
        );

        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    void 멤버십삭제성공() throws Exception {
        final String url = "/api/v1/memberShip/-1";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
        );

        resultActions.andExpect(status().isNoContent());
    }

    @Test
    void 멤버십적립실패_사용자식별값이헤더에없음() throws Exception {

        final String url = "/api/v1/memberShip/-1/accumulate";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .content(gson.toJson(memberShipRequest(10000,MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    void 멤버십적립실패_포인트가음수() throws Exception {
        final String url = "/api/v1/memberShip/-1/accumulate";

        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(-1, MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 멤버십적립성공() throws Exception {
        // given
        final String url = "/api/v1/memberShip/-1/accumulate";

        // when
        final ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(membershipRequest(10000)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNoContent());
    }
    private MemberShipRequest membershipRequest(final Integer point) {
        return MemberShipRequest.builder()
                                .point(point)
                                .build();
    }
}
