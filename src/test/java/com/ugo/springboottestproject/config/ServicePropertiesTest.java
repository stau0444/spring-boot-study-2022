package com.ugo.springboottestproject.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ServicePropertiesTest {
    @Test
    void test(@Autowired ServiceProperties serviceProperties){
        Assertions.assertThat(serviceProperties.isEnabled()).isFalse();
    }
}