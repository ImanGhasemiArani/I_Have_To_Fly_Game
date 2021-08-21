package com.example.flying2dgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    private float screenRatioX, screenRatioY;
    private int screenX, screenY;
    private Paint paint;
    private Background background1, background2;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        int assign
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;
        paint = new Paint();
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

    }//Constructor method

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
        }//while

    }//run

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//sleep

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }//draw

    private void update() {

        background1.x -=  10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }//if
        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }//if

    }//update

    public void pause() {
        try {
            isPlaying = false;
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//pause

    public void resume() {
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }//resume

}//GameView
