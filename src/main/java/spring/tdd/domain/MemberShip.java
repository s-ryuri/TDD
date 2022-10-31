package spring.tdd.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Entity
@Table(name = "member_ship")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberShip_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "memberShip_name")
    private MemberShipType memberShipType;

    private int point;

    @Builder
    public MemberShip(Long id, MemberShipType memberShipType, int point) {
        this.id = id;
        this.memberShipType = memberShipType;
        this.point = point;
    }
}
