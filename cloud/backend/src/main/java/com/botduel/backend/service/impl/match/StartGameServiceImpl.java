package com.botduel.backend.service.impl.match;

import com.botduel.backend.service.match.StartGameService;
import com.botduel.backend.websocket.WebSocketServer;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotid) {
        System.out.println("start game: " + aId + " " + bId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotid);
        return "start game success";
    }

}
