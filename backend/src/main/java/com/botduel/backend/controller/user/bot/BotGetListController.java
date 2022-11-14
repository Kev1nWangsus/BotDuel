package com.botduel.backend.controller.user.bot;

import com.botduel.backend.pojo.Bot;
import com.botduel.backend.service.user.bot.BotGetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BotGetListController {
    @Autowired
    BotGetListService botGetListService;

    @GetMapping("/user/bot/getlist/")
    public List<Bot> getList() {
        return botGetListService.getList();
    }
}
