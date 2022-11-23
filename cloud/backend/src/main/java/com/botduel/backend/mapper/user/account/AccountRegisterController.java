package com.botduel.backend.mapper.user.account;

import com.botduel.backend.service.user.account.AccountRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountRegisterController {

    @Autowired
    private AccountRegisterService accountRegisterService;

    @PostMapping("/user/account/register/")
    public Map<String, String> register(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return accountRegisterService.register(username, password, confirmedPassword);
    }
}
