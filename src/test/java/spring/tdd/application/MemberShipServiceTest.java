package spring.tdd.application;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;
import spring.tdd.infra.MemberShipRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class MemberShipServiceTest {

    private final String userId = "userId";
    private final MemberShipType membershipType = MemberShipType.NAVER;
    private final Integer point = 10000;

    @InjectMocks
    private MemberShipService memberShipService;

    @Mock
    private MemberShipRepository memberShipRepository;

    @Test
    void 멤버십_이미존재_등록실패() {
        //given
        MemberShip memberShip = MemberShip.builder()
                                          .userId("아아디!")
                                          .point(1000)
                                          .memberShipType(MemberShipType.KAKAO)
                                          .build();

        doReturn(memberShip)
            .when(memberShipRepository).findByUserIdAndMemberShipType(userId, membershipType);

        MemberShipException result = assertThrows(MemberShipException.class,
                                                  () -> memberShipService.registerMemberShip(userId, membershipType, point));

        assertThat(result.getErrorResult()).isEqualTo(MembShipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }
}
