package com.botduel.backend.service.impl.user.account;

import com.botduel.backend.pojo.User;
import com.botduel.backend.service.impl.utils.UserUtil;
import com.botduel.backend.service.user.account.AccountInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountInfoServiceImpl implements AccountInfoService {
    @Override
    public Map<String, String> getInfo() {
        User user = UserUtil.getUser();

        Map<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }
}
