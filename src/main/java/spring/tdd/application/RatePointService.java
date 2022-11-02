package spring.tdd.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatePointService implements PointService{

    private static final int POINT_RATE = 1;
    public int calculateAmount(final int price){
        return price * POINT_RATE / 100;
    }
}
