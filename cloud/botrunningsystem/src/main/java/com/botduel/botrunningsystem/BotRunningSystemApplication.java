package com.botduel.botrunningsystem;

import com.botduel.botrunningsystem.service.impl.BotRunningServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.BOT_POOL.start();
        SpringApplication.run(BotRunningSystemApplication.class, args);
    }
}