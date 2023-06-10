package com.example.a0504randomball;

import static android.graphics.Color.DKGRAY;
import static android.graphics.Color.GREEN;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.Random;

public class Portal {
    private int portalinX;
    private int portalinY;
    private int portalOutX;
    private int portalOutY;

    public int[][] portalMap;

    public int[][] getPortalMap() {
        return portalMap;
    }

    String porStruct;

    public Portal(int[][] mapss) {
        porStruct =
                        "                    "+
                        "                  O "+
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
                        "                    "+
                        " I                  "+
                        "                    ";
    }

    public void DrawPortal(Canvas cvs, int Xw, int Xh, int[][] portalmaze) {
        int cellSize = cvs.getWidth() / Xw;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                int cellValue = portalmaze[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                if (cellValue == 3) {
                    paint.setColor(GREEN);
                    cvs.drawRect(left, top, right, bottom, paint);
                } else if (cellValue == 4) {
                    paint.setColor(DKGRAY);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }

    public int[][] createPortal(int Xw, int Xh) {
        int[][] temp = new int[Xh][Xw];
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                if (i == 3 && j == 3) {
                    temp[i][j] = 3;
                    setPortalinX(j);
                    setPortalinY(i);
                } else if (i == 9 & j == 9) {
                    temp[i][j] = 4;
                    setPortalOutX(j);
                    setPortalOutY(i);
                }
            }
        }
        this.portalMap = temp;
        return temp;
    }
    private void generatePortalPosition() {
        setPortalinX(3);
        setPortalinY(3);
        setPortalOutX(10);
        setPortalOutY(10);
    }
    public int getPortalinX() {
        return portalinX;
    }

    public void setPortalinX(int portalinX) {
        this.portalinX = portalinX;
    }

    public int getPortalinY() {
        return portalinY;
    }

    public void setPortalinY(int portalinY) {
        this.portalinY = portalinY;
    }

    public int getPortalOutX() {
        return portalOutX;
    }

    public void setPortalOutX(int portalOutX) {
        this.portalOutX = portalOutX;
    }

    public int getPortalOutY() {
        return portalOutY;
    }

    public void setPortalOutY(int portalOutY) {
        this.portalOutY = portalOutY;
    }
}
