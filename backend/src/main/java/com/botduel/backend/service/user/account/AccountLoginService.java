package com.botduel.backend.service.user.account;

import java.util.Map;

public interface AccountLoginService {
    public Map<String, String> getToken(String username, String password);
}
