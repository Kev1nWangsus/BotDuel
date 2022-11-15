package com.botduel.backend.service.impl.user.bot;

import com.botduel.backend.mapper.BotMapper;
import com.botduel.backend.pojo.Bot;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.impl.utils.UserDetailsImpl;
import com.botduel.backend.service.impl.utils.UserUtil;
import com.botduel.backend.service.user.bot.BotUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BotUpdateServiceImpl implements BotUpdateService {
    @Autowired
    BotMapper botMapper;

    @Override
    public Map<String, String> update(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();

        Map<String, String> map = new HashMap<>();

        int botId = Integer.parseInt(data.get("botId"));
        Bot bot = botMapper.selectById(botId);

        if (bot == null) {
            map.put("error_message", "Bot does not exist");
            return map;
        }

        if (!bot.getUserId().equals(user.getId())) {
            map.put("error_message", "You can only modify your bot");
            return map;
        }

        String title = data.get("title");
        String description = data.get("description");
        String code = data.get("code");

        final int TITLE_LIMIT = 100;
        final int DESCRIPTION_LIMIT = 300;
        final int CODE_LIMIT = 10000;

        if (title == null || title.length() == 0) {
            map.put("error_message", "Title cannot be empty");
            return map;
        }

        if (title.length() > TITLE_LIMIT) {
            map.put("error_message", "Title cannot exceed 100");
            return map;
        }

        if (description == null || description.length() == 0) {
            description = "This user is lazy lol";
        }

        if (description.length() > DESCRIPTION_LIMIT) {
            map.put("error_message", "Description cannot exceed 300");
            return map;
        }

        if (code == null || code.length() == 0) {
            map.put("error_message", "Code cannot be empty");
            return map;
        }

        if (code.length() > CODE_LIMIT) {
            map.put("error_message", "Code cannot exceed 10000");
            return map;
        }

        Bot newBot = new Bot(
                bot.getId(),
                user.getId(),
                title,
                description,
                code,
                bot.getRating(),
                bot.getCreateTime(),
                new Date()
        );

        botMapper.updateById(newBot);
        map.put("error_message", "Updated successfully");
        return map;
    }
}
