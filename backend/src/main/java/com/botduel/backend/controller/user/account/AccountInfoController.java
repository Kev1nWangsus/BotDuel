package com.botduel.backend.controller.user.account;

import com.botduel.backend.service.user.account.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AccountInfoController {
    @Autowired
    private AccountInfoService accountInfoService;

    @GetMapping("/user/account/info/")
    public Map<String, String> getInfo() {
        return accountInfoService.getInfo();
    }
}
