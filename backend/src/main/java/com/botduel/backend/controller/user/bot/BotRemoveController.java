package com.botduel.backend.controller.user.bot;

import com.botduel.backend.service.user.bot.BotRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BotRemoveController {
    @Autowired
    private BotRemoveService botRemoveService;

    @PostMapping("/user/bot/remove/")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        return botRemoveService.remove(data);
    }
}