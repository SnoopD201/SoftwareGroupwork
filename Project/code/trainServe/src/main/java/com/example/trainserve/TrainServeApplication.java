package com.example.trainserve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.trainServe.mapper")
public class TrainServeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TrainServeApplication.class, args);
    }

}
