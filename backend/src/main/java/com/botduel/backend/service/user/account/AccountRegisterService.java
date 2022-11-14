package com.botduel.backend.service.user.account;

import java.util.Map;

public interface AccountRegisterService {
    public Map<String, String> register(String username, String password, String confirmedPassword);
}
