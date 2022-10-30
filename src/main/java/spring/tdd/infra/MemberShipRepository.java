package spring.tdd.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.tdd.domain.MemberShip;

public interface MemberShipRepository extends JpaRepository<MemberShip,Long> {
}
