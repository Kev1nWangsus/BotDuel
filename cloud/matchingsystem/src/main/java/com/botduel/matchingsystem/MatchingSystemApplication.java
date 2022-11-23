package com.botduel.matchingsystem;

import com.botduel.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchingSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.MATCHING_POOL.start();
        SpringApplication.run(MatchingSystemApplication.class, args);
    }
}