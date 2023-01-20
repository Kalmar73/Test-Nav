package com.agro.test_nav;

import com.agro.test_nav.Model.NavData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestNavApplicationTests {

    @Test
    void contextLoads() {
        NavData navData = new NavData("src/main/resources/nmea.log");
        System.out.println(navData.countPath());
    }

}
