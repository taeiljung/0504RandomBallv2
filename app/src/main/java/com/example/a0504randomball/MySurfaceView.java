package com.example.a0504randomball;

import static android.graphics.Color.*;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MyThread thread;
    static int[][] maze_map;
    static int[][] player_map;
    static int[][] portal_map;
    static int g_width = 15;
    static Maze maze = new Maze(g_width);
    static Player pyr = new Player(maze);
    static Monster mtr = new Monster(maze);
    static Portal por = new Portal(pyr.mapss);
    static SurfaceHolder holder;
    public MySurfaceView(Context ctx) {
        super(ctx);
        holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        thread = new MyThread(holder);
        maze_map = maze.createMaze(maze.width, maze.height, maze.mapStruct);
        player_map = pyr.createPlayer(maze.width, maze.height, pyr.pyrStruct);
        portal_map = por.createPortal(maze.width, maze.height);
        pyr.setPortalMaze(por);
    }

    public MyThread getThread() {
        return thread;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        restartGame();  // 게임을 초기화
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public class MyThread extends Thread {
        private boolean mRun = false;
        private final SurfaceHolder mSurfaceHolder;
        private long mDelay = 500;

        public MyThread(SurfaceHolder holder) {
            mSurfaceHolder = holder;
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    if (c != null) {
                        c.drawColor(BLACK);
                        synchronized (mSurfaceHolder) {
                            maze.drawMap(c, maze.width, maze.height, maze_map);
                            pyr.drawPlayer(c, maze.width, maze.height, player_map);
                            mtr.drawMonster(c,maze.width,maze.height);
                            updateMonster(); // 몬스터 업데이트
                            por.DrawPortal(c, maze.width, maze.height, por.portalMap);

                            invalidate();
                            if (pyr._clear) {
                                System.out.println("클리어당");
                                restartGame();
                                pyr._clear = false;

                            }
                        }
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }

                try {
                    Thread.sleep(mDelay); // 일정한 대기 시간 후에 다음 루프로 진행
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        public void updateMonster() {
            // 몬스터 업데이트 로직 구현
            mtr.updateMonster();
        }
        public void setRunning(boolean b) {
            mRun = b;
        }

        public void resetGame() {
            // 게임을 초기화하는 로직을 구현
            maze_map = maze.createMaze(maze.width, maze.height, maze.mapStruct);
            player_map = pyr.createPlayer(maze.width, maze.height, pyr.pyrStruct);
            portal_map = por.createPortal(maze.width, maze.height);
            pyr.setPortalMaze(por);
        }
    }

    public void restartGame() {
        thread.resetGame();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                pyr.movePlayer(Direction.UP);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                pyr.movePlayer(Direction.DOWN);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                pyr.movePlayer(Direction.LEFT);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                pyr.movePlayer(Direction.RIGHT);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}



class Ball {

    int x,y,xInc=1,yInc=1;
    int diameter;
    static int WIDTH = 1080, HEIGHT = 1920;
    Random rand = new Random();
    int myC= rand.nextInt()*255;
    public Ball(int d){
        int a = (int) (Math.random()*40);
        this.diameter = d+a;
        x = (int) (Math.random() * (WIDTH - d) + 3);
        y = (int) (Math.random() * (HEIGHT - d) + 3);

        xInc = (int) (Math.random() * 5 + 1);
        yInc = (int) (Math.random() * 5 + 1);
    }
    public void paint(Canvas g) {
        Paint paint = new Paint();
        if(x < 0 || x > (WIDTH-diameter))
            xInc = -xInc;
        if(y < 0 || y > (HEIGHT-diameter))
            yInc = -yInc;
        x += xInc;
        y += yInc;
        paint.setColor(argb(255,myC,myC,myC));
        g.drawCircle(x,y,diameter,paint);
    }
    public void Rpaint() {
        xInc = -xInc;
        yInc = -yInc;
    }
}

class Square  {
    int x,y,xInc=1,yInc=1;
    int diameter;
    static int WIDTH = 1080, HEIGHT = 1920;
    Random rand = new Random();
    int myC= rand.nextInt()*255;
    public Square(int d){
        int a = (int) (Math.random()*40);
        this.diameter = d+a;
        x = (int) (Math.random() * (WIDTH - d) + 3);
        y = (int) (Math.random() * (HEIGHT - d) + 3);

        xInc = (int) (Math.random() * 5 + 1);
        yInc = (int) (Math.random() * 5 + 1);
    }
    public void paint(Canvas g) {
        Paint paint = new Paint();
        if(x < 0 || x > (WIDTH-diameter))
            xInc = -xInc;
        if(y < 0 || y > (HEIGHT-diameter))
            yInc = -yInc;
        x += xInc;
        y += yInc;
        paint.setColor(argb(255,myC,myC,myC));
        g.drawRect(x,y,x+80,y+80,paint);
    }
    public void Rpaint() {
        xInc = -xInc;
        yInc = -yInc;
    }
}

class Maze {
    public int[][] MazeMap;
    public String mapStruct;
    public int width;
    public int height;

    public Maze(int widths) {
        this.width = widths;
        mapStruct = "###############" +
                "      #       #" +
                "#     #       #" +
                "#     #       #" +
                "#     #       #" +
                "#     #       #" +
                "####     ###  #" +
                "#             #" +
                "#   ######    #" +
                "#     #       #" +
                "#     #       #" +
                "#     #       #" +
                "#     #       #" +
                "#     #        " +
                "###############";
        this.height = generatedHeight();
        this.MazeMap = createMaze(width, height, mapStruct);
    }

    public void drawMap(Canvas cvs, int Xw, int Xh, int[][] maze) {
        int cellSize = cvs.getWidth() / Xw;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
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
class Player {
    public boolean _clear = false;
    private int playerX;
    private int playerY;
    public String pyrStruct;
    public int[][] mapss;
    private Maze maze;
    private Portal portalmaze;
    public int[][] portalmaps;
    public Player(Maze maze) {
        this.maze = maze;
        pyrStruct =
                        "               " +
                        "P              " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "              C" +
                        "               ";
        this.mapss = createPlayer(maze.width,maze.height, pyrStruct);
    }
    public void drawPlayer(Canvas cvs, int Xw, int Xh, int[][] pmaze) {
        int cellSize = cvs.getWidth() / Xw;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j < Xw; j++) {
                int cellValue = pmaze[i][j];
                int left = j * cellSize;
                int top = i * cellSize;
                int right = left + cellSize;
                int bottom = top + cellSize;
                Paint paint = new Paint();
                if (cellValue == 1) {
                    paint.setColor(BLUE);
                    cvs.drawRect(left, top, right, bottom, paint);
                }else if(cellValue == 2){
                    paint.setColor(YELLOW);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }
    public void setPortalMaze(Portal portalmap){
        this.portalmaze = portalmap;
        this.portalmaps = portalmaze.portalMap;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int[][] createPlayer(int Xw,int Xh, String maps) {
        int[][] temp = new int[Xh][Xw];
        int index = 0;
        for (int i = 0; i < Xh; i++) {
            for (int j = 0; j <Xw; j++) {
                char c = maps.charAt(index);
                if (c == 'P') {
                    temp[i][j] = 1;
                    setPlayerX(j);
                    setPlayerY(i);
                } else if(c == 'C'){
                    temp[i][j] = 2;
                }else{
                    temp[i][j] = 0;
                }
                index++;
            }
        }
        mapss = temp;
        return mapss;
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
            if(portalmaze.getPortalinY() == newY && portalmaze.getPortalinX() == newX){
                setPlayerX(portalmaze.getPortalOutX());
                setPlayerY(portalmaze.getPortalOutY());

            }
        }
    }

    private boolean checkMovable(int newX, int newY) {
        if (newX >= 0 && newX < maze.width && newY >= 0 && newY < maze.height) {
            if (maze.MazegetMap()[newY][newX] != 1) {
                if(getPMap()[newY][newX] == 2){ // clear 여부
                    this._clear = true;
                }
                return true;
            }
        }
        return false;
    }
    private boolean checkPORTALIn(int newX, int newY) {
        if (newY == portalmaze.getPortalinY() && newX == portalmaze.getPortalinX()) {
                System.out.println(newY+" : "+newX);
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
class Portal{
    private int portalinX;
    private int portalinY;
    private int portalOutX;
    private int portalOutY;

    public int[][] portalMap;
    public int[][] getPortalMap(){
        return portalMap;
    }
    String porStruct ;

    public Portal(int[][] mapss) {
        porStruct =
                        "               " +
                        "               " +
                        "   I           " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "               " +
                        "        O      " +
                        "               " +
                        "               " +
                        "               " +
                        "               ";
    }
    public void DrawPortal(Canvas cvs, int Xw,int Xh, int[][] portalmaze){
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
                }else if(cellValue == 4){
                    paint.setColor(DKGRAY);
                    cvs.drawRect(left, top, right, bottom, paint);
                }
            }
        }
    }
    public int[][] createPortal(int Xw, int Xh){
        int[][] temp = new int[Xh][Xw];
            for (int i = 0; i < Xh; i++) {
                for (int j = 0; j <Xw; j++) {
                    if(i==3 && j==3) {
                        temp[i][j] = 3;
                        setPortalinX(j);
                        setPortalinY(i);
                    }else if(i == 9 & j == 9){
                        temp[i][j] = 4;
                        setPortalOutX(j);
                        setPortalOutY(i);
                    }
                }
            }
            this.portalMap = temp;
        return temp;
    }
    public void Incheck(){

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



class Monster {
    private int monsterX;
    private int monsterY;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private long moveDelay; // Monster의 이동 속도 조절을 위한 변수
    long lastMoveTime;


    public Monster(Maze maze) {
        this.map = maze.MazegetMap();
        this.mapHeight = maze.height;
        this.mapWidth = maze.width;
        generateRandomPosition();
        this.moveDelay = moveDelay; // 이동 속도 변수 초기화
        map[monsterY][monsterX] = 4; // Monster의 위치를 설정하고 맵에 표시
        lastMoveTime = System.currentTimeMillis();
    }

    public void drawMonster(Canvas cvs, int Xw, int Xh) {
        int cellSize = cvs.getWidth() / Xw;
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

    public void updateMonster() {
        // 맵 탐색 및 이동 로직을 구현하세요
        // 현재 위치에서 이동 가능한 경로를 탐색하여 이동합니다

        int[] dx = {0, 0, -1, 1}; // 상하좌우 이동에 대한 x 좌표 변화량
        int[] dy = {-1, 1, 0, 0}; // 상하좌우 이동에 대한 y 좌표 변화량

        // 랜덤한 방향으로
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime >= moveDelay) {
            lastMoveTime = currentTime;

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

    private boolean checkMovable(int newX, int newY) {
        // 이동 가능한지 체크 (예: 경계 체크, 벽 체크 등)
        if (newX >= 1 && newX < mapWidth-1 && newY >= 1 && newY < mapHeight-1) {
            if(map[newY][newX] != 1){
                return map[newY][newX] != 4;
            }
        }
        return false;
    }

    private void generateRandomPosition() {
        Random random = new Random();
        this.monsterX = random.nextInt(mapWidth);
        this.monsterY = random.nextInt(mapHeight);
    }
}