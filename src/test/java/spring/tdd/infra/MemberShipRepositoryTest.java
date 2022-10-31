package spring.tdd.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class MemberShipRepositoryTest {

    @Autowired
    private MemberShipRepository memberShipRepository;

    @Test
    @DisplayName("MemberShipRepository가 Bean이 등록되어있는 지 확인")
    void MemberShipRepositoryNotNull() {
        assertThat(memberShipRepository).isNotNull();
    }

    @Test
    @DisplayName("멤버십이 제대로 등록되는 지 확인")
    void 멤버쉽등록() {
        MemberShip memberShip = MemberShip.builder()
                                          .userId("userId")
                                          .memberShipType(MemberShipType.NAVER)
                                          .point(10000)
                                          .build();

        MemberShip save = memberShipRepository.save(memberShip);

        assertThat(save.getId()).isNotNull();
        assertThat(save.getMemberShipType()).isEqualTo(MemberShipType.NAVER);
        assertThat(save.getPoint()).isEqualTo(10000);
    }

    @Test
    @DisplayName("멤버쉽 존재 테스트")
    void 멤버십이_존재하는지_테스트() {
        MemberShip memberShip = MemberShip.builder()
                                          .userId("아이디!")
                                          .memberShipType(MemberShipType.NAVER)
                                          .point(10000)
                                          .build();

        memberShipRepository.save(memberShip);

        MemberShip findResult = memberShipRepository.findByUserIdAndMemberShipType("아이디!",MemberShipType.NAVER);

        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getId()).isNotNull();
        assertThat(findResult.getUserId()).isEqualTo("userId");
        assertThat(findResult.getMemberShipType()).isEqualTo(MemberShipType.NAVER);
        assertThat(findResult.getPoint()).isEqualTo(10000);
    }
}
