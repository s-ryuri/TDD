package spring.tdd.api;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import spring.tdd.domain.MemberShipType;

@Getter
@Builder
@RequiredArgsConstructor
public class MemberShipAddResponse {

    private final Long id;
    private final MemberShipType memberShipType;
}
