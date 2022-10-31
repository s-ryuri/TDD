package spring.tdd.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;

public interface MemberShipRepository extends JpaRepository<MemberShip,Long> {
    MemberShip findByUserIdAndMemberShipType(String userId, MemberShipType memberShipType);
}
