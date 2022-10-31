package spring.tdd.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.tdd.domain.MemberShipType;

@Getter
@NoArgsConstructor
public class MemberShipRequest {

    private int point;
    private MemberShipType memberShipType;

    @Builder
    public MemberShipRequest(int point, MemberShipType memberShipType) {
        this.point = point;
        this.memberShipType = memberShipType;
    }
}
