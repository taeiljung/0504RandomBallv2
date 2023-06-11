package com.example.a0504randomball;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class Monster {
    private int monsterX;
    private int monsterY;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private static int mState = 0;


    private long moveDelay; // Monster의 이동 속도 조절을 위한 변수
    long lastMoveTime;

    public static void setState(int x) {
        mState = x;

    }

    public Monster(Maze maze) {
        this.map = maze.MazegetMap();
        this.mapHeight = maze.height;
        this.mapWidth = maze.width;
        generateRandomPosition(this.map);
        this.moveDelay = moveDelay;
        lastMoveTime = System.currentTimeMillis();
    }

    public void drawMonster(Canvas cvs) {
        int cellSize = cvs.getWidth() / mapWidth;
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                int cellValue = map[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                if (cellValue == 4) {
                    paint.setColor(Color.RED);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }

    public void updateMonster(Player player) {
        int playerX;
        int playerY;
        int[] dx = {0, 0, -1, 1}; // 상하좌우 이동에 대한 x 좌표 변화량
        int[] dy = {-1, 1, 0, 0}; // 상하좌우 이동에 대한 y 좌표 변화량

        // 랜덤한 방향으로 이동 또는 플레이어를 쫓는 로직
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveDelay) {
            lastMoveTime = currentTime;

            playerX = player.getPlayerX();
            playerY = player.getPlayerY();

            int distanceX = playerX - monsterX;
            int distanceY = playerY - monsterY;
//            if() {
            if (Math.abs(distanceX) <= 4 && Math.abs(distanceY) <= 4 && mState ==0) {
                // 플레이어를 쫓는 로직 수행
                int directionX = Integer.compare(distanceX, 0);
                int directionY = Integer.compare(distanceY, 0);
                // 대각선 이동 제한
                if (Math.abs(distanceX) != Math.abs(distanceY)) {
                    // 대각선 거리가 4 이내인 경우에만 이동을 수행
                    int newX = monsterX + directionX;
                    int newY = monsterY + directionY;

                    // 이동 가능한지 체크
                    if (checkMovable(newX, monsterY)) {
                        map[monsterY][monsterX] = 0;
                        monsterX = newX;
                        map[monsterY][monsterX] = 4;
                    } else if (checkMovable(monsterX, newY)) {
                        map[monsterY][monsterX] = 0;
                        monsterY = newY;
                        map[monsterY][monsterX] = 4;
                    }
                } else {
                    // 대각선 이동이 불가능한 경우 상하좌우 이동을 수행
                    int newX = monsterX + directionX;
                    int newY = monsterY + directionY;

                    // 이동 가능한지 체크
                    if (checkMovable(newX, monsterY)) {
                        map[monsterY][monsterX] = 0;
                        monsterX = newX;
                        map[monsterY][monsterX] = 4;
                    } else if (checkMovable(monsterX, newY)) {
                        map[monsterY][monsterX] = 0;
                        monsterY = newY;
                        map[monsterY][monsterX] = 4;
                    }
                }

            } else {
                // 랜덤한 방향으로 이동
                Random random = new Random();
                int direction = random.nextInt(4); // 0부터 3까지의 랜덤한 정수
                int newX = monsterX + dx[direction];
                int newY = monsterY + dy[direction];

                // 이동 가능한지 체크
                if (checkMovable(newX, newY)) {
                    map[monsterY][monsterX] = 0;
                    monsterX = newX;
                    monsterY = newY;
                    map[monsterY][monsterX] = 4;
                }
            }
        }
    }


    private boolean checkMovable(int newX, int newY) {
        // 이동 가능한지 체크 (예: 경계 체크, 벽 체크 등)
        if (newX >= 1 && newX < mapWidth - 1 && newY >= 1 && newY < mapHeight - 1) {
            if (map[newY][newX] != 1) {
                return map[newY][newX] != 4;
            }
        }
        return false;
    }

    private void generateRandomPosition(int[][] mazeMaps) {
        int[][] mazeMap = mazeMaps;
        int emptyCellCount = 0;

        // Count the number of empty cells in the maze
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (mazeMap[i][j] == 0) {
                    emptyCellCount++;
                }
            }
        }

        Random random = new Random();
        int randomCellIndex = random.nextInt(emptyCellCount);

        // Find the corresponding coordinates of the random cell
        int cellCounter = 0;
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (mazeMap[i][j] == 0) {
                    if (cellCounter == randomCellIndex) {
                        monsterX = j;
                        monsterY = i;
                        return;
                    }
                    cellCounter++;
                }
            }
        }
    }


    public int getMonsterX() {
        return monsterX;
    }

    public int getMonsterY() {
        return monsterY;
    }
}