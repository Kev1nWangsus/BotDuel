package com.botduel.backend.service.impl.user.bot;

import com.botduel.backend.mapper.BotMapper;
import com.botduel.backend.pojo.Bot;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.impl.utils.UserUtil;
import com.botduel.backend.service.user.bot.BotRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BotRemoveServiceImpl implements BotRemoveService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {
        User user = UserUtil.getUser();

        int botId = Integer.parseInt(data.get("bot_id"));
        Bot bot = botMapper.selectById(botId);

        Map<String, String> map = new HashMap<>();

        if (bot == null) {
            map.put("error_message", "Bot does not exist");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "You can only remove your bot");
            return map;
        }

        botMapper.deleteById(botId);
        map.put("error_message", "success");
        return map;
    }
}
