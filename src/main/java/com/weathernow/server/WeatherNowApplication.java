package com.weathernow.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(value = {"com.weathernow.server.mapper"})
@SpringBootApplication
public class WeatherNowApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherNowApplication.class, args);
	}

}
