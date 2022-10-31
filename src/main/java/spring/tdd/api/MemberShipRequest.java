package spring.tdd.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.tdd.domain.MemberShipType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberShipRequest {

    @NotNull
    @Min(0)
    private Integer point;

    @NotNull
    private MemberShipType memberShipType;

    @Builder
    public MemberShipRequest(Integer point, MemberShipType memberShipType) {
        this.point = point;
        this.memberShipType = memberShipType;
    }
}
