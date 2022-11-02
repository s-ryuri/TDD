package spring.tdd.api;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.tdd.application.ValidationGroups;
import spring.tdd.application.ValidationGroups.MemberShipAccmulateMarker;
import spring.tdd.application.ValidationGroups.MemberShipAddMarker;
import spring.tdd.domain.MemberShipType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberShipRequest {

    @NotNull(groups = {MemberShipAccmulateMarker.class, MemberShipAddMarker.class})
    @Min(value = 0,groups = {MemberShipAccmulateMarker.class, MemberShipAddMarker.class})
    private Integer point;

    @NotNull(groups = {MemberShipAddMarker.class})
    private MemberShipType memberShipType;

    @Builder
    public MemberShipRequest(Integer point, MemberShipType memberShipType) {
        this.point = point;
        this.memberShipType = memberShipType;
    }
}
