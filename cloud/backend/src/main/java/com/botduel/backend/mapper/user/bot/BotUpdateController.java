package com.botduel.backend.mapper.user.bot;

import com.botduel.backend.service.user.bot.BotUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BotUpdateController {
    @Autowired
    private BotUpdateService botUpdateService;

    @PostMapping("/user/bot/update/")
    public Map<String, String> create(@RequestParam Map<String, String> data) {
        return botUpdateService.update(data);
    }
}
