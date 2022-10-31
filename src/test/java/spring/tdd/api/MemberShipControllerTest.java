package spring.tdd.api;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import spring.tdd.application.MemberShipErrorResult;
import spring.tdd.application.MemberShipException;
import spring.tdd.application.MemberShipService;
import spring.tdd.domain.MemberShipType;

import static org.assertj.core.api.Assertions.*;
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

    @Test
    void 멤버십등록실패_포인트가Null() throws Exception {

        //given
        final String url = "/api/v1/memberShip";

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(null, MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    void 멤버십등록실패_포인트가음수() throws Exception {

        //given
        final String url = "/api/v1/memberShip";

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(-1, MemberShipType.KAKAO)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());

    }

    @Test
    void 멤버십등록실패_멤버십종류가Null() throws Exception {

        //given
        final String url = "/api/v1/memberShip";

        //when
        ResultActions resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post(url)
                                  .header(MemberShipConstants.USER_ID_HEAER, "12345")
                                  .content(gson.toJson(memberShipRequest(-1, null)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isBadRequest());
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
                                  .content(gson.toJson(memberShipRequest(-1, null)))
                                  .contentType(MediaType.APPLICATION_JSON)
        );

        //then
        resultActions.andExpect(status().isBadRequest());
    }
}
