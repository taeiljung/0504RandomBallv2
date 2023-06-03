package com.example.a0504randomball;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.util.Random;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    public static Ball[] basket = new Ball[5];
    public static Square[] sbasket = new Square[5];
    private MyThread thread;
    private Path path = new Path();

    public MySurfaceView(Context ctx) {
        super(ctx);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        thread = new MyThread(holder);
        for(int i =0; i<5;i++){
            basket[i] = new Ball(20);
            }
        for(int i =0; i<5;i++){
            sbasket[i] = new Square(20);
        }

    }
    public MyThread getThread(){
        return thread;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while(retry){
            try{
                thread.join();
                retry = false;
            }catch (InterruptedException e){
            }
        }
    }
    public class MyThread extends Thread{
        private boolean mRun = false;
        private final SurfaceHolder mSurfaceHolder;
        public MyThread(SurfaceHolder holder) {
            mSurfaceHolder = holder;
        }
        @Override
        public void run() {
            while(mRun) {
                Canvas c = null;
                try{
                    c = mSurfaceHolder.lockCanvas(null);
                    c.drawColor(Color.BLACK);
                    synchronized (mSurfaceHolder){
                        for (Ball b : basket){
                            b.paint(c);
                        }
                        for (Square ss : sbasket){
                            ss.paint(c);
                        }
                    }
                }finally {
                    if(c!=null){
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
            super.run();
        }

        public void setRunning(boolean b) {
            mRun = b;
        }
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
        paint.setColor(Color.argb(255,myC,myC,myC));
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
        paint.setColor(Color.argb(255,myC,myC,myC));
        g.drawRect(x,y,x+80,y+80,paint);
    }
    public void Rpaint() {
        xInc = -xInc;
        yInc = -yInc;
    }
}

