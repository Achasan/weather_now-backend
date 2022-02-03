package com.weathernow.server.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TimeMapperTest {

    @Autowired
    private TimeMapper timeMapper;


    // MyBatis, Oracle 연동 테스트 : select sysdate from dual
    @Test
    public void loggingTime() {
        log.info("class Name is {}", timeMapper.getClass().getName());
        log.info("time is {}", timeMapper.getTime());
    }
}
