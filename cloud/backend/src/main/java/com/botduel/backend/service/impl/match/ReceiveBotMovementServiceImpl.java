package com.botduel.backend.service.impl.match;

import com.botduel.backend.service.match.ReceiveBotMovementService;
import com.botduel.backend.websocket.WebSocketServer;
import com.botduel.backend.websocket.utils.Game;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMovementServiceImpl implements ReceiveBotMovementService {
    @Override
    public String receiveBotMovement(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction + " ");
        if (WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if (game != null) {
                if (game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if (game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }

        return "receive bot move success";
    }

}
