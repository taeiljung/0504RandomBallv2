package com.example.a0504randomball;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class Player {
    public boolean _clear = false;
    private int playerX;
    private int playerY;
    public String pyrStruct;
    public int[][] mapss;
    private Maze maze;
    private Portal portalmaze;
    public int[][] portalmaps;
    private int mapWidth;
    private int mapHeight;
    public static ArrayList<Integer> hideXList;
    public static ArrayList<Integer> hideYList;
    public static int pState = 0;


    public Player(Maze maze) {
        this.mapHeight = maze.height;
        this.mapWidth = maze.width;

        this.maze = maze;
        pyrStruct =
                "                    "+
                        "P                   "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                   C"+
                        "                    "+
                        "                    ";
        createPlayer(maze.width, maze.height, pyrStruct);
    }

    public static void setpState(int pStates) {
        pState = pStates;
    }

    public void drawPlayer(Canvas cvs, int[][] pmaze,int nowState) {
        int cellSize = cvs.getWidth() / mapWidth;
        if(nowState == 0){
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                int cellValue = pmaze[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                if (cellValue == 1) {
                    paint.setColor(BLUE);
                    cvs.drawRect(left, top, right, bottom, paint);
                } else if (cellValue == 2) {
                    paint.setColor(YELLOW);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
        }else{
            for (int i = 0; i < mapHeight; i++) {
                for (int j = 0; j < mapWidth; j++) {
                    int cellValue = pmaze[i][j];
                    int left = j * cellSize;
                    int top = i * cellSize;
                    int right = left + cellSize;
                    int bottom = top + cellSize;
                    Paint paint = new Paint();
                    if (cellValue == 1) {
                        paint.setColor(WHITE);
                        cvs.drawRect(left, top, right, bottom, paint);
                    } else if (cellValue == 2) {
                        paint.setColor(YELLOW);
                        cvs.drawRect(left, top, right, bottom, paint);
                    }
                }
            }
        }
    }

    public void setPortalMaze(Portal portalmap) {
        this.portalmaze = portalmap;
        this.portalmaps = portalmaze.portalMap;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int[][] createPlayer(int Xw, int Xh, String maps) {
        int[][] temp = new int[Xh][Xw];
        int index = 0;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                char c = maps.charAt(index);
                if (c == 'P') {
                    temp[i][j] = 1;
                    setPlayerX(j);
                    setPlayerY(i);
                } else if (c == 'C') {
                    temp[i][j] = 2;
                } else {
                    temp[i][j] = 0;
                }
                index++;
            }
        }
        mapss = temp;
        return mapss;
    }
    public static void getXYList(ArrayList<Integer> myXlist, ArrayList<Integer> myYlist){
        hideXList = myXlist;
        hideYList = myYlist;
    }

    public void movePlayer(Direction direction) {
        int newX = getPlayerX();
        int newY = getPlayerY();

        switch (direction) {
            case UP:
                newY = getPlayerY() - 1;
                break;
            case DOWN:
                newY = getPlayerY() + 1;
                break;
            case LEFT:
                newX = getPlayerX() - 1;
                break;
            case RIGHT:
                newX = getPlayerX() + 1;
                break;
        }

        if (checkMovable(newX, newY)) {
            mapss[getPlayerY()][getPlayerX()] = 0;
            mapss[newY][newX] = 1;
            setPlayerX(newX);
            setPlayerY(newY);
            if (checkPORTALIn(newX, newY)) {
                setPlayerX(portalmaze.getPortalOutX());
                setPlayerY(portalmaze.getPortalOutY());
            }

            if(checkHIDEIn(newX,newY)){
                Monster.setState(1);
                setpState(1);
            }else{
                Monster.setState(0);
                setpState(0);
            }
        }

    }

    private boolean checkHIDEIn(int newX, int newY) {
        for (int i=0;i< hideXList.size();i++){
            if(newX == hideXList.get(i) && newY == hideYList.get(i)){
                return true;
            }
        }
        return false;
    }

    private boolean checkMovable(int newX, int newY) {
        if (newX >= 0 && newX < maze.width && newY >= 0 && newY < maze.height) {
            if (maze.MazegetMap()[newY][newX] != 1) {
                if (getPMap()[newY][newX] == 2) { // clear 여부
                    this._clear = true;
                }
                return true;
            }
        }
        return false;
    }

    private boolean checkPORTALIn(int newX, int newY) {
        if (portalmaze.getPortalinY() == newY && portalmaze.getPortalinX() == newX) {
            return true;
        }
        return false;
    }

    private void setPlayerY(int newY) {
        playerY = newY;
    }

    private void setPlayerX(int newX) {
        playerX = newX;
    }

    public int[][] getPMap() {
        return mapss;
    }
}
