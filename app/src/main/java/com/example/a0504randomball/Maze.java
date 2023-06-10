package com.example.a0504randomball;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Maze {
    public int[][] MazeMap;
    public String mapStruct;
    public int width;
    public int height;

    public Maze(int widths) {
        this.width = widths;
        mapStruct =
                        "####################"+
                        "            #    # #"+
                        "# # # ### # #### # #"+
                        "#   #   # #        #"+
                        "# ### # # # # # ####"+
                        "# #       # # #    #"+
                        "# ####### # # # #  #"+
                        "# #     # # ### #  #"+
                        "# ##### #   #   #  #"+
                        "# #       # # # #  #"+
                        "#   ##### # # # ## #"+
                        "# #     # #   #  # #"+
                        "# ##### # ###### #  "+
                        "#     # #      # # #"+
                        "####################";
        this.height = generatedHeight();
        this.MazeMap = createMaze(width, height, mapStruct);
    }

    public void drawMap(Canvas cvs, int[][] maze) {
        int cellSize = cvs.getWidth() / width;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int cellValue = maze[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                paint.setColor(cellValue == 1 ? BLACK : WHITE);
                cvs.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public int generatedHeight() {
        return mapStruct.length() / width;
    }

    public int[][] createMaze(int Xw, int Xh, String maps) {
        int[][] temp = new int[Xh][Xw];
        int index = 0;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                char c = maps.charAt(index);
                if (c == '#') {
                    temp[i][j] = 1;
                } else {
                    temp[i][j] = 0;
                }
                index++;
            }
        }
        return temp;
    }

    public int[][] MazegetMap() {
        return MazeMap;
    }
}
