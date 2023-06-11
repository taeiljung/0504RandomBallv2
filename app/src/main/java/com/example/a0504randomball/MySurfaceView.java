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
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder holder;

    private ArrayList<Monster> monsters;

    private Thread thread;
    private static Maze maze;
    private static Player pyr;
    private static Hide hide;
    private int monsterCount = 5;
    private static Portal por;
    private static int[][] maze_map;
    private static int[][] player_map;
    private static int[][] hide_map;
    private static int[][] portal_map;
    private static int g_width = 20;
    private Direction nowDirection;
    private Handler handler;


    public MySurfaceView(Context ctx) {
        super(ctx);
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
        por = new Portal(pyr.mapss);
        maze_map = maze.createMaze(maze.width, maze.height, maze.mapStruct);
        player_map = pyr.createPlayer(maze.width, maze.height, pyr.pyrStruct);
        hide_map = hide.createHide(maze.width, maze.height, hide.HideStruct);
        portal_map = por.createPortal(maze.width, maze.height);
        nowDirection = Direction.LEFT;
        pyr.setPortalMaze(por);

    }

    public Thread getThread() {
        return thread;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        restartGame();
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
                            if (checkGameOver(pyr.pState)) {
                                setRunning(false);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getContext().startActivity(intent);
                                ((Activity) getContext()).finish();


                                //이런슈발
//                                handler.post(new Runnable(){
//                                    @Override
//                                    public void run() {
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                                        builder.setTitle("게임 종료")
//                                                .setMessage("게임이 종료되었습니다.")
//                                                .setPositiveButton("STOP", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        dialog.dismiss(); // 다이얼로그 닫기
//
//                                                        Intent intent = new Intent(getContext(), MainActivity.class);
//                                                        getContext().startActivity(intent);
//                                                        ((Activity) getContext()).finish();
//                                                    }
//                                                })
//                                                .setNegativeButton("RETRY", new DialogInterface.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(DialogInterface dialog, int i) {
//                                                        dialog.dismiss(); // 다이얼로그 닫기
//                                                        setRunning(true);
//                                                        restartGame();
//                                                    }
//                                                });
//
//                                        AlertDialog alertDialog = builder.create();
//                                        alertDialog.setCancelable(false); // 다이얼로그가 백 버튼 등으로 닫히지 않도록 설정
//                                        alertDialog.show();
//                                    }
//                                });
                            }
                            por.DrawPortal(c, maze.width, maze.height, por.portalMap);
                            int left = 0;
                            int top = getTop()+800;
                            int right = getWidth();
                            int bottom = getBottom();
                            Paint paint = new Paint();
                            paint.setColor(Color.CYAN);
                            c.drawRect(left, top, right, bottom, paint);
                            invalidate();

                            if (pyr._clear) {
//                                System.out.println("클리어당");
//                                restartGame();
                                setRunning(false);
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                getContext().startActivity(intent);
                                ((Activity) getContext()).finish();
                                pyr._clear = false;
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
}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}
