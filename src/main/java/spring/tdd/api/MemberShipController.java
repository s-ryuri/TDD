package spring.tdd.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.tdd.application.MemberShipResponse;
import spring.tdd.application.MemberShipService;

import javax.validation.Valid;

import java.util.List;

import static spring.tdd.api.MemberShipConstants.USER_ID_HEAER;

@RestController
@RequestMapping("/api/v1/memberShip")
@RequiredArgsConstructor
public class MemberShipController {

    private final MemberShipService memberShipService;

    @GetMapping("")
    public ResponseEntity<List<MemberShipDetailResponse>> getMemberShipList(
        @RequestHeader(USER_ID_HEAER) final String userId
    ) {
        return ResponseEntity.ok(memberShipService.getMemberShipList(userId));
    }

    @PostMapping("")
    public ResponseEntity<MemberShipResponse> registerMemberShip(
        @RequestHeader(USER_ID_HEAER) final String userId,
        @RequestBody @Valid final MemberShipRequest memberShipRequest
    ) {

        final MemberShipResponse memberShipResponse = memberShipService.registerMemberShip(userId,
                                                                                           memberShipRequest.getMemberShipType(),
                                                                                           memberShipRequest.getPoint());

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(memberShipResponse);
    }
}
