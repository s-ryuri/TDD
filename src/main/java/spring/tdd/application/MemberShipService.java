package spring.tdd.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.tdd.api.MemberShipDetailResponse;
import spring.tdd.domain.MemberShip;
import spring.tdd.domain.MemberShipType;
import spring.tdd.infra.MemberShipRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberShipService {

    private final MemberShipRepository memberShipRepository;

    @Transactional
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

    public List<MemberShipDetailResponse> getMemberShipList(final String userId) {
        List<MemberShip> memberShips = memberShipRepository.findAllByUserId(userId);
        return memberShips.stream()
            .map(v -> MemberShipDetailResponse
                .builder()
                .id(v.getId())
                .memberShipType(v.getMemberShipType())
                .point(v.getPoint())
                .build())
            .collect(Collectors.toList());
    }
}
