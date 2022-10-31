package spring.tdd.application;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.tdd.domain.MemberShipType;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberShipResponse {

    private final Long id;
    private final MemberShipType memberShipType;


}
