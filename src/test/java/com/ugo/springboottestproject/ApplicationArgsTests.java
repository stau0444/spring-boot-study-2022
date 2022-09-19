package com.ugo.springboottestproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(args = {"--app.name=trail-log-test"})
public class ApplicationArgsTests {
    @Test
    void testApplicationArgs(@Autowired ApplicationArguments args){
        args.getOptionNames().contains("app.name");
        assertThat(args.getOptionNames().contains("app.name"));
        assertThat(args.getOptionValues("app.name").contains("trail-log-test"));
    }
}
