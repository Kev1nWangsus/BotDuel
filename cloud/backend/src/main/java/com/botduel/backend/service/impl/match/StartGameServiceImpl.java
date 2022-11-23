package com.botduel.backend.service.impl.match;

import com.botduel.backend.service.match.StartGameService;
import com.botduel.backend.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("Start game:" + aId + " " + bId);
        WebSocketServer.startGame(aId, bId);
        return "Game started successfully";
    }
}
