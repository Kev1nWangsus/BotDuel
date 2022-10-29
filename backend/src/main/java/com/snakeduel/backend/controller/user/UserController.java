package com.snakeduel.backend.controller.user;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.snakeduel.backend.mapper.UserMapper;
import com.snakeduel.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @GetMapping("/user/all/")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    @GetMapping("/user/{userId}/")
    public User getUser(@PathVariable int userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        return userMapper.selectOne(queryWrapper);
    }

    @GetMapping("/user/add/{userId}/{username}/{password}")
    public String addUser(
            @PathVariable int userId,
            @PathVariable String username,
            @PathVariable String password) {
        User user = new User(userId, username, password);
        userMapper.insert(user);
        return "Created a user";
    }
    @GetMapping("/user/delete/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userMapper.deleteById(userId);
        return "Delete a user";
    }

}
