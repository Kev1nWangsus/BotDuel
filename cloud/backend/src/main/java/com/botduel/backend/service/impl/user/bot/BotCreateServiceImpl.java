package com.botduel.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.botduel.backend.mapper.BotMapper;
import com.botduel.backend.pojo.Bot;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.impl.utils.UserUtil;
import com.botduel.backend.service.user.bot.BotCreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BotCreateServiceImpl implements BotCreateService {

    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> create(Map<String, String> data) {

        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
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

        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        if (botMapper.selectCount(queryWrapper) >= 10) {
            map.put("error_message", "One user can only create 10 bots!");
            return map;
        }

        Date date = new Date();
        Bot bot = new Bot(null, user.getId(), title, description, code, date, date);

        botMapper.insert(bot);
        map.put("error_message", "success");
        return map;
    }
}
