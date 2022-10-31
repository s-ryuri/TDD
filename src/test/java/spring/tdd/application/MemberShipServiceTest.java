package spring.tdd.application;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;
import spring.tdd.infra.MemberShipRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

        assertThat(result.getMemberShipErrorResult()).isEqualTo(MemberShipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
    }

    @Test
    void 멤버십등록성공(){

        //given
        doReturn(null).when(memberShipRepository).findByUserIdAndMemberShipType(userId,membershipType);
        doReturn(memberShip()).when(memberShipRepository).save(Mockito.any(MemberShip.class));

        //when
        MemberShipResponse memberShip = memberShipService.registerMemberShip(userId, membershipType, point);

        //then
        assertThat(memberShip.getId()).isNotNull();
        assertThat(memberShip.getMemberShipType()).isEqualTo(MemberShipType.NAVER);

        //verify
        verify(memberShipRepository,times(1)).findByUserIdAndMemberShipType(userId,membershipType);
        verify(memberShipRepository,times(1)).save(any(MemberShip.class));

    }

    private MemberShip memberShip() {
        return MemberShip.builder()
            .id(-1L)
            .userId(userId)
            .point(point)
            .memberShipType(MemberShipType.NAVER)
            .build();
    }
}
