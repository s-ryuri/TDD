package spring.tdd.application;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberShipException extends RuntimeException{

    private final MemberShipErrorResult memberShipErrorResult;
}
