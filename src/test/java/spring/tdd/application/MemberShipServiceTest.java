package spring.tdd.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.tdd.api.MemberShipDetailResponse;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;
import spring.tdd.infra.MemberShipRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    void 멤버십등록성공() {

        //given
        doReturn(null).when(memberShipRepository).findByUserIdAndMemberShipType(userId, membershipType);
        doReturn(memberShip()).when(memberShipRepository).save(Mockito.any(MemberShip.class));

        //when
        MemberShipResponse memberShip = memberShipService.registerMemberShip(userId, membershipType, point);

        //then
        assertThat(memberShip.getId()).isNotNull();
        assertThat(memberShip.getMemberShipType()).isEqualTo(MemberShipType.NAVER);

        //verify
        verify(memberShipRepository, times(1)).findByUserIdAndMemberShipType(userId, membershipType);
        verify(memberShipRepository, times(1)).save(any(MemberShip.class));

    }

    private MemberShip memberShip() {
        return MemberShip.builder()
                         .id(-1L)
                         .userId(userId)
                         .point(point)
                         .memberShipType(MemberShipType.NAVER)
                         .build();
    }

    @Test
    void 멤버십목록조회() {

        //given
        List<MemberShip> memberShips = Arrays.asList(
            MemberShip.builder().build(),
            MemberShip.builder().build(),
            MemberShip.builder().build()
        );

        doReturn(memberShips)
            .when(memberShipRepository).findAllByUserId(userId);

        //when
        final List<MemberShipDetailResponse> result = memberShipService.getMemberShipList(userId);

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    void 멤버십상세조회실패_존재하지않음() {
        doReturn(Optional.empty())
            .when(memberShipRepository)
            .findById(12345L);

        final MemberShipException memberShipException = assertThrows(MemberShipException.class,
                                                                     () -> memberShipService.getMemberShip(12345L, userId));

        assertThat(memberShipException.getMemberShipErrorResult()).isEqualTo(MemberShipErrorResult.MEMBERSHIP_NOT_FOUND);

    }

    @Test
    public void 멤버십상세조회실패_본인이아님() {
        // given
        doReturn(Optional.empty()).when(memberShipRepository).findById(12345L);

        // when
        final MemberShipException result = assertThrows(MemberShipException.class, () -> memberShipService.getMemberShip(12345L, "notowner"));

        // then
        assertThat(result.getMemberShipErrorResult()).isEqualTo(MemberShipErrorResult.MEMBERSHIP_NOT_FOUND);
    }

    @Test
    public void 멤버십상세조회성공() {
        // given
        doReturn(Optional.of(MemberShip.builder()
                                 .memberShipType(MemberShipType.NAVER)
                                 .point(10000)
                                 .userId(userId)
                                 .build())).when(memberShipRepository).findById(12345L);

        // when
        final MemberShipDetailResponse result = memberShipService.getMemberShip(12345L, userId);

        // then
        assertThat(result.getMemberShipType()).isEqualTo(MemberShipType.NAVER);
        assertThat(result.getPoint()).isEqualTo(point);
    }
}
