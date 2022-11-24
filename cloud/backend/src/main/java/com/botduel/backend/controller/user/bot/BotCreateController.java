package com.botduel.backend.controller.user.bot;

import com.botduel.backend.service.user.bot.BotCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BotCreateController {
    @Autowired
    private BotCreateService botCreateService;

    @PostMapping("/user/bot/create/")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        return botCreateService.create(data);
    }
}
