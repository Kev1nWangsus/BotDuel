package com.botduel.backend.service.impl.user.bot;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.botduel.backend.mapper.BotMapper;
import com.botduel.backend.pojo.Bot;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.impl.utils.UserUtil;
import com.botduel.backend.service.user.bot.BotGetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BotGetListServiceImpl implements BotGetListService {
    @Autowired
    BotMapper botMapper;

    @Override
    public List<Bot> getList() {
        User user = UserUtil.getUser();
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId());
        return botMapper.selectList(queryWrapper);
    }
}
