package com.botduel.backend.controller.user.account;

import com.botduel.backend.service.user.account.AccountLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountLoginController {
    @Autowired
    private AccountLoginService accountLoginService;

    @PostMapping("/user/account/token/")
    public Map<String, String> getToken(@RequestParam Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        return accountLoginService.getToken(username, password);
    }
}
