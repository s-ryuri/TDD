package spring.tdd.infra;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
