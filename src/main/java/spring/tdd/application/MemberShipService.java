package spring.tdd.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;
import spring.tdd.infra.MemberShipRepository;

@Service
@RequiredArgsConstructor
public class MemberShipService {

    private final MemberShipRepository memberShipRepository;

    public MemberShipResponse registerMemberShip(String userId, MemberShipType memberShipType, int price) {
        MemberShip byUserIdAndMemberShipType = memberShipRepository.findByUserIdAndMemberShipType(userId, memberShipType);
        if (byUserIdAndMemberShipType != null) {
            throw new MemberShipException(MemberShipErrorResult.DUPLICATED_MEMBERSHIP_REGISTER);
        }

        MemberShip build = MemberShip.builder()
                                     .userId(userId)
                                     .memberShipType(memberShipType)
                                     .point(price)
                                     .build();

        MemberShip save = memberShipRepository.save(build);

        return MemberShipResponse.builder()
                                 .id(save.getId())
                                 .memberShipType(save.getMemberShipType())
                                 .build();
    }
}
