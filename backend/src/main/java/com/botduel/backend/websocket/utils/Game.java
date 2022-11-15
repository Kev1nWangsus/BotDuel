package com.botduel.backend.websocket.utils;

import java.util.Random;

public class Game {
    final private Integer rows;
    final private Integer cols;
    final private Integer innerWallsCount;

    final private int[][] gameMap;
    final private static int[] dx = {-1, 0, 1, 0};
    final private static int[] dy = {0, -1, 0, 1};

    public Game(Integer rows, Integer cols, Integer innerWallsCount) {
        this.rows = rows;
        this.cols = cols;
        this.innerWallsCount = innerWallsCount;
        this.gameMap = new int[rows][cols];
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
            gameMap[r][0] = gameMap[r][this.cols-1] = 1;
        }

        for (int c = 0; c < this.rows; c++) {
            gameMap[0][c] = gameMap[this.rows-1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.innerWallsCount; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (gameMap[r][c] == 1 || gameMap[this.rows-1-r][this.cols-1-c] == 1) {
                    continue;
                }

                if ((r == this.rows - 2 && c == 1) || (r == 1 && c == this.cols - 2)) {
                    continue;
                }

                gameMap[r][c] = gameMap[this.rows-1-r][this.cols-1-c] = 1;
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
}
