package com.example.a0504randomball;

import static android.graphics.Color.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder holder;

    private ArrayList<Monster> monsters;
    private long startTime;
    private Thread thread;
    private static Maze maze;
    private static Player pyr;
    private static Hide hide;
    private static int monsterCount = 1;
    private static Portal por;
    private static int[][] maze_map;
    private static int[][] player_map;
    private static int[][] hide_map;
    private static int[][] portal_map;
    private static int g_width = 20;
    private Direction nowDirection;
    private Handler handler;
    private Activity mActivity;

    public MySurfaceView(Context ctx, Activity activity) {
        super(ctx);
        mActivity = activity;
        holder = getHolder();
        holder.addCallback(this);
        handler = new Handler(Looper.getMainLooper());
        setFocusable(true);
        maze = new Maze(g_width);
        hide = new Hide(maze);
        pyr = new Player(maze);

        monsters = new ArrayList<>();
        for (int i = 0; i < monsterCount; i++) {
            Monster monster = new Monster(maze);
            monsters.add(monster);
        }
        por = new Portal();
        maze_map = maze.createMaze(maze.width, maze.height, maze.mapStruct);
        player_map = pyr.createPlayer(maze.width, maze.height, pyr.pyrStruct);
        hide_map = hide.createHide(maze.width, maze.height, hide.HideStruct);
        portal_map = por.createPortal(maze.width, maze.height);
        nowDirection = Direction.LEFT;
        pyr.setPortalMaze(por);

    }

    public static void setMonsterCount(int mCount) {
        System.out.println("setMonsterCount >> "+ mCount);
        monsterCount = mCount;
    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        restartGame();
        startTime = System.currentTimeMillis();
        thread = new Thread(new MyRunnable());
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        thread.interrupt();
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private class MyRunnable implements Runnable {
        private boolean mRun = true;
        private long mDelay = 100;
        private int count = 0;
        private int pyrcount = 0;

        public void setRunning(boolean b) {
            mRun = b;
            if (!mRun) {
                thread.interrupt();
            }
        }


        @Override
        public void run() {
            Canvas c;
            while (mRun && !Thread.currentThread().isInterrupted()) {
                c = null;

                try {
                    c = holder.lockCanvas(null);
                    if (c != null) {
                        synchronized (holder) {
                            c.drawColor(WHITE);
                            maze.drawMap(c, maze_map);
                            hide.DrawHide(c, hide_map);
                            pyr.drawPlayer(c, player_map, pyr.pState);
                            for (Monster monster : monsters) {
                                monster.drawMonster(c);
                            }
                            if (count > 10) {
                                updateMonster();
                                count = 0;
                            }
                            if (pyrcount > 8) {
                                updatePlayer();
                                pyrcount = 0;
                            }
                            long elapsedTime = System.currentTimeMillis() - startTime;
                            int seconds = (int) (elapsedTime / 1000); // 경과 시간(초)

                            // 화면에 플레이 시간 표시
                            Paint textPaint = new Paint();
                            textPaint.setColor(Color.WHITE);
                            textPaint.setTextSize(40);
                            String timeText = "플레이 시간: " + seconds + "초";
                            c.drawText(timeText, getWidth() - textPaint.measureText(timeText) - 20, 50, textPaint);

                            if (checkGameOver(pyr.pState)) {
                                setRunning(false);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialog(seconds,1);
                                    }
                                });
                            }
                            por.DrawPortal(c, maze.width, maze.height, por.portalMap);
                            int left = 0;
                            int top = getTop()+800;
                            int right = getWidth();
                            int bottom = getBottom();
                            Paint paint = new Paint();
                            paint.setColor(Color.CYAN);
                            c.drawRect(left, top, right, bottom, paint);
                            showKeyboard(c);
                            invalidate();

                            if (pyr._clear) {
                                System.out.println("클리어당");
                                setRunning(false);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        showAlertDialog(seconds,0);
                                    }
                                });
                            }
                            try {
                                if(mRun){
                                    Thread.sleep(1);
                                    count++;
                                    pyrcount++;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }
            }
        }
        private void showAlertDialog(int seconds,int type) {
            String temp = "";
            if(monsterCount == 1 ){
                temp = "LV1";
            }else if(monsterCount == 5 ){
                temp = "LV2";
            }else if(monsterCount == 10 ){
                temp = "LV3";
            }

            if(type == 1){
                new AlertDialog.Builder(mActivity)
                        .setTitle("게임종료")
                        .setMessage("고작"+seconds+"초? .. 다시 도전해보세요. ㅋ\n"+temp+"실패!")
                        .setNegativeButton("알아서 할게요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("NOPE.");
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                mActivity.startActivity(intent);
                                mActivity.finish();
                            }
                        })
                        .show();
            }else{
            new AlertDialog.Builder(mActivity)
                    .setTitle("승리!")
                    .setMessage("겨우"+seconds+"초 버텼습니다 !\n"+temp+"달성!")
                    .setNegativeButton("잘 놀다 갑니다", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.out.println("WIN.");
                            Intent intent = new Intent(mActivity, MainActivity.class);
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        }
                    })
                    .show();
            }
        }

        public void updateMonster() {
            for (Monster monster : monsters) {
                monster.updateMonster(pyr);
            }

            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void updatePlayer() {
            pyr.movePlayer(nowDirection);
            try {
                Thread.sleep(mDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private boolean checkGameOver(int pState) {
            for (Monster monster : monsters) {
                if (pyr.getPlayerX() == monster.getMonsterX() && pyr.getPlayerY() == monster.getMonsterY() && pState == 0) {
                    return true;
                }
            }
            return false;
        }


    }//runnable()


    public void restartGame() {
        maze_map = maze.createMaze(maze.width, maze.height, maze.mapStruct);
        player_map = pyr.createPlayer(maze.width, maze.height, pyr.pyrStruct);
        portal_map = por.createPortal(maze.width, maze.height);

//        for (int i = 0; i < monsterCount; i++) {
//            Monster monster = new Monster(maze);
//            monsters.add(monster);
//        }
        pyr.setPortalMaze(por);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                nowDirection = Direction.UP;
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                nowDirection = Direction.DOWN;
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                nowDirection = Direction.LEFT;
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                nowDirection = Direction.RIGHT;
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void showKeyboard(Canvas c){
        int buttonWidth = getWidth() / 4;
        int buttonHeight = getHeight() / 5;
        int buttonTop = getHeight() - buttonHeight;
        int buttonBottom = getHeight();

        Paint paint = new Paint();
        paint.setTextSize(400f);  // 텍스트 크기 조절
        paint.setColor(Color.BLACK);
        c.drawRect(0, buttonTop, buttonWidth, buttonBottom, paint);
        paint.setColor(Color.WHITE);
        drawCenteredText(c, "↑", 0, buttonTop, buttonWidth, buttonBottom, paint);

        paint.setColor(Color.BLACK);
        c.drawRect(buttonWidth, buttonTop, buttonWidth * 2, buttonBottom, paint);
        paint.setColor(Color.WHITE);
        drawCenteredText(c, "↓", buttonWidth, buttonTop, buttonWidth * 2, buttonBottom, paint);

        paint.setColor(Color.BLACK);
        c.drawRect(buttonWidth * 2, buttonTop, buttonWidth * 3, buttonBottom, paint);
        paint.setColor(Color.WHITE);
        drawCenteredText(c, "←", buttonWidth * 2, buttonTop, buttonWidth * 3, buttonBottom, paint);

        paint.setColor(Color.BLACK);
        c.drawRect(buttonWidth * 3, buttonTop, getWidth(), buttonBottom, paint);
        paint.setColor(Color.WHITE);
        drawCenteredText(c, "→", buttonWidth * 3, buttonTop, getWidth(), buttonBottom, paint);

    }
    private void drawCenteredText(Canvas canvas, String text, float left, float top, float right, float bottom, Paint paint) {
        // 텍스트 크기 계산
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        float textWidth = paint.measureText(text);
        float textHeight = textBounds.height();

        // 텍스트를 버튼 내에 가운데로 그리기
        float x = (left + right) / 2 - textWidth / 2;
        float y = (top + bottom) / 2 + textHeight / 2;
        canvas.drawText(text, x, y, paint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                onClick(x, y);
                break;
        }

        return true;
    }

    public void onClick(float x, float y) {
        int buttonWidth = getWidth() / 4;
        int buttonHeight = getHeight() / 5;
        int buttonTop = getHeight() - buttonHeight;
        int buttonBottom = getHeight();

        if (y >= buttonTop && y <= buttonBottom) {
            if (x >= 0 && x < buttonWidth) {
                nowDirection = Direction.UP;
                System.out.println("up");
            } else if (x >= buttonWidth && x < buttonWidth * 2) {
                nowDirection = Direction.DOWN;
                System.out.println("down");
            } else if (x >= buttonWidth * 2 && x < buttonWidth * 3) {
                nowDirection = Direction.LEFT;
                System.out.println("left");
            } else if (x >= buttonWidth * 3 && x < getWidth()) {
                nowDirection = Direction.RIGHT;
                System.out.println("right");
            }
        }
    }



}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}
