package spring.tdd.api;

import com.google.gson.Gson;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemberShipControllerTest {

    private MemberShipController memberShipController;

    private MockMvc mockMvc;
    private Gson gson;

    @Test
    void mockMvc가NUll이아님() throws Exception {
        assertThat(memberShipController).isNotNull();
        assertThat(mockMvc).isNotNull();

    }
}
