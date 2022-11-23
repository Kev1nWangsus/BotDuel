package com.botduel.matchingsystem.service.impl;

import com.botduel.matchingsystem.service.MatchingService;
import com.botduel.matchingsystem.service.impl.utils.MatchingPool;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    public final static MatchingPool MATCHING_POOL = new MatchingPool();

    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("add player: " + userId + " " + rating);
        MATCHING_POOL.addPlayer(userId, rating, botId);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player: " + userId);
        MATCHING_POOL.removePlayer(userId);
        return "remove player success";
    }

}
