package com.botduel.backend.websocket.utils;

import com.alibaba.fastjson.JSONObject;
import com.botduel.backend.pojo.Record;
import com.botduel.backend.websocket.WebSocketServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    private final static int[] dx = {-1, 0, 1, 0};
    private final static int[] dy = {0, -1, 0, 1};
    private final Integer rows;
    private final Integer cols;
    private final Integer innerWallsCount;
    private final int[][] gameMap;
    private final Player playerA;

    private final Player playerB;
    private final ReentrantLock lock = new ReentrantLock();
    private Integer nextStepA = null;
    private Integer nextStepB = null;
    private String status = "playing";  // playing, finished

    private String loser = "";    // a, b, tie

    public Game(Integer rows, Integer cols, Integer innerWallsCount, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.gameMap = new int[rows][cols];
        this.playerA = new Player(idA, rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(idB, 1, cols - 2, new ArrayList<>());
    }

    public Player getPlayerA() {
        return playerA;
    }

    public Player getPlayerB() {
        return playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getGameMap() {
        return gameMap;
    }

    private boolean checkConnectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) {
            return true;
        }

        gameMap[sx][sy] = 1;
        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i], y = sy + dy[i];
            if (x >= 0 && x < this.rows && y >= 0 && y < this.cols && gameMap[x][y] == 0) {
                if (checkConnectivity(x, y, tx, ty)) {
                    gameMap[sx][sy] = 0;
                    return true;
                }
            }
        }

        gameMap[sx][sy] = 0;
        return false;
    }

    public boolean draw() {
        for (int r = 0; r < this.rows; r++) {
            for (int c = 0; c < this.cols; c++) {
                gameMap[r][c] = 0;
            }
        }

        for (int r = 0; r < this.rows; r++) {
            gameMap[r][0] = gameMap[r][this.cols - 1] = 1;
        }

        for (int c = 0; c < this.rows; c++) {
            gameMap[0][c] = gameMap[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (gameMap[r][c] == 1 || gameMap[this.rows - 1 - r][this.cols - 1 - c] == 1) {
                    continue;
                }

                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2)) {
                    continue;
                }

                gameMap[r][c] = gameMap[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        return checkConnectivity(this.rows - 2, 1, 1, this.cols - 2);
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            if (draw()) {
                break;
            }
        }
    }

    private boolean nextStep() {
        // wait for next steps (5 secs)
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 50; i++) {
            try {
                // wait for 1 sec
                Thread.sleep(100);
                lock.unlock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        break;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private boolean isValid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell lastCell = cellsA.get(n - 1);
        if (gameMap[lastCell.x][lastCell.y] == 1) {
            return false;
        }

        for (int i = 0; i < n - 1; i++) {
            if (cellsA.get(i).x == lastCell.x && cellsA.get(i).y == lastCell.y) {
                return false;
            }
        }

        for (int i = 0; i < n - 1; i++) {
            if (cellsB.get(i).x == lastCell.x && cellsB.get(i).y == lastCell.y) {
                return false;
            }
        }
        return true;
    }

    private void judge() {
        // check if two players' moves are allowed
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean isValidA = isValid(cellsA, cellsB);
        boolean isValidB = isValid(cellsB, cellsA);
        if (!isValidA || !isValidB) {
            status = "finished";

            if (!isValidA && !isValidB) {
                loser = "all";
            } else if (!isValidA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private void sendAllMessages(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove() {
        // broadcast each player's move to the other
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessages(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(gameMap[i][j]);
            }
        }
        return res.toString();
    }


    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepString(),
                playerB.getStepString(),
                getMapString(),
                loser,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }


    private void sendResult() {
        // broadcast results to two clients
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        saveToDatabase();
        sendAllMessages(resp.toJSONString());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            if (nextStep()) {
                judge();
                if ("playing".equals(status)) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                lock.lock();

                try {
                    if (nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if (nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }
}
