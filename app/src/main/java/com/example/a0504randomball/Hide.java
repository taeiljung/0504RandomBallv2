package com.example.a0504randomball;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.YELLOW;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

public class Hide {
    private Maze MazeMaps;
    private int[][] Hidemap;

    private int mapHeight;
    private int mapWidth;
    private int hideX;
    private int hideY;
    public ArrayList<Integer> myXlist;
    public ArrayList<Integer> myYlist;
    public String HideStruct;

    public Hide(Maze maze){
        this.MazeMaps = maze;
        this.mapHeight = maze.height;
        this.mapWidth = maze.width;
        myXlist = new ArrayList<>();
        myYlist = new ArrayList<>();
        HideStruct =
                        "                    "+
                        "H         H       H "+
                        "                    "+
                        "                  H "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                H   "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                    "+
                        "                H   "+
                        "                    ";
        createHide(mapWidth,mapHeight, this.HideStruct);
    }
    public int[][] createHide(int Xw, int Xh, String maps) {
        int[][] temp = new int[Xh][Xw];
        int index = 0;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                char c = maps.charAt(index);
                if (c == 'H') {
                    temp[i][j] = 9;
                    myXlist.add(j);
                    myYlist.add(i);
                }else {
                    temp[i][j] = 0;
                }
                index++;
            }
        }
        Player.getXYList(myXlist,myYlist);
        Hidemap = temp;
        return Hidemap;
    }
    public void DrawHide(Canvas cvs, int[][] maze) {
        int cellSize = cvs.getWidth() / this.mapWidth;
        for (int i = 0; i < this.mapHeight; i++) {
            for (int j = 0; j < this.mapWidth; j++) {
                int cellValue = maze[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                if (cellValue == 9) {
                    paint.setColor(Color.CYAN);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }

    public int getHideX() {
        return hideX;
    }

    public void setHideX(int hideX) {
        this.hideX = hideX;
    }

    public int getHideY() {
        return hideY;
    }

    public void setHideY(int hideY) {
        this.hideY = hideY;
    }
}
