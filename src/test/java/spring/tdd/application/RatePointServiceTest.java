package spring.tdd.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RatePointServiceTest {

    @InjectMocks
    private RatePointService ratePointService;

    @Test
    void _10000원의적립은100원(){
        final int price = 10000;

        final int result = ratePointService.calculateAmount(price);

        assertThat(result).isEqualTo(100);
    }

    @Test
    void _20000원의적립은200원(){
        final int price = 20000;

        final int result = ratePointService.calculateAmount(price);

        assertThat(result).isEqualTo(200);
    }

    @Test
    void _30000원의적립은300원(){
        final int price = 30000;

        final int result = ratePointService.calculateAmount(price);

        assertThat(result).isEqualTo(300);
    }
}
