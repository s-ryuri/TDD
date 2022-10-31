package spring.tdd.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberShipErrorResult {

    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    UNKNOWN_EXCEPTION(HttpStatus.BAD_REQUEST,"500 Error"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND,"MemberShip Not Found"),
    NOT_MEMBERSHIP_OWNER(HttpStatus.BAD_REQUEST,"NOT_OWNER");

    private final HttpStatus httpStatus;
    private final String message;
}
