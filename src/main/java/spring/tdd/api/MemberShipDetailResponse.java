package spring.tdd.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import spring.tdd.domain.MemberShipType;

@Getter
@NoArgsConstructor
public class MemberShipDetailResponse {

    private Long id;
    private MemberShipType memberShipType;
    private Integer point;

    @Builder
    public MemberShipDetailResponse(Long id, MemberShipType memberShipType, Integer point) {
        this.id = id;
        this.memberShipType = memberShipType;
        this.point = point;
    }
}
