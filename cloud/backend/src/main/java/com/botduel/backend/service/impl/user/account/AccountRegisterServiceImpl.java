package com.botduel.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.botduel.backend.mapper.UserMapper;
import com.botduel.backend.pojo.User;
import com.botduel.backend.service.user.account.AccountRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AccountRegisterServiceImpl implements AccountRegisterService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> map = new HashMap<>();
        if (username == null) {
            map.put("error_message", "Username cannot be empty");
            return map;
        }

        if (password == null || confirmedPassword == null) {
            map.put("error_message", "Password cannot be empty");
            return map;
        }

        username = username.trim();
        if (username.length() == 0) {
            map.put("error_message", "Username cannot be empty");
            return map;
        }

        if (password.length() == 0 || confirmedPassword.length() == 0) {
            map.put("error_message", "Username cannot be empty");
            return map;
        }

        if (username.length() > 100) {
            map.put("error_message", "Username cannot exceed 100");
            return map;
        }

        if (password.length() > 100 || confirmedPassword.length() > 100) {
            map.put("error_message", "Password cannot exceed 100");
            return map;
        }

        if (!password.equals(confirmedPassword)) {
            map.put("error_message", "Passwords are not the same");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<User> users = userMapper.selectList(queryWrapper);
        if (!users.isEmpty()) {
            map.put("error_message", "User already exists");
            return map;
        }

        String encodedPassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/237677_lg_aba03d5c4d.jpg";

        User user = new User(null, username, encodedPassword, photo, 1500);
        userMapper.insert(user);
        map.put("error_message", "success");
        return map;
    }
}
