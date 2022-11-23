package com.botduel.matchingsystem.service.impl;

import com.botduel.matchingsystem.service.MatchingService;
import org.springframework.stereotype.Service;

@Service
public class MatchingServiceImpl implements MatchingService {
    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("Add player: " + userId + " " + rating);
        return "Added user successfully!";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("Remove player: " + userId);
        return "Remove player successfully";
    }
}
