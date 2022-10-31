package spring.tdd.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
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

    @Column(name = "memberShip_name")
    private String name;

    private int point;

    @Builder
    public MemberShip(Long id, String name, int point) {
        this.id = id;
        this.name = name;
        this.point = point;
    }
}
