package spring.tdd.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static spring.tdd.api.MemberShipConstants.USER_ID_HEAER;

@RestController
@RequestMapping("/api/v1/memberShip")
@RequiredArgsConstructor
public class MemberShipController {

    @PostMapping("")
    public ResponseEntity<MemberShipRequest> registerMemberShip(
        @RequestHeader(USER_ID_HEAER) final String userId,
        @RequestBody final MemberShipRequest memberShipRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
