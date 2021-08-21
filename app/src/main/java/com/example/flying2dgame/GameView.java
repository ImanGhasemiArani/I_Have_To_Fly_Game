package com.example.flying2dgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {
    private Thread gameThread;
    private boolean isPlaying;
    public static float screenRatioX, screenRatioY;
    private int screenX, screenY;
    private Paint paint;
    private Background background1, background2;
    private Flight flight;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        this.screenX = screenX;
        this.screenY = screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;
        paint = new Paint();
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;
        flight = new Flight(screenY, getResources());

    }//Constructor method

    @Override
    public void run() {

        while (isPlaying) {
            update();
            draw();
            sleep();
        }//while

    }//run

    private void update() {

        //update x's backgrounds
        background1.x -=  10 * screenRatioX;
        background2.x -= 10 * screenRatioX;

        //conditions' update for moving background
        if (background1.x + background1.background.getWidth() < 0)
            background1.x = screenX;

        if (background2.x + background2.background.getWidth() < 0)
            background2.x = screenX;

        //update y's flight
        if (flight.isGoingUp)
            flight.y -= 30 * screenRatioY;
        else
            flight.y += 30 * screenRatioY;

        //conditions' update for flight
        if (flight.y < 0)
            flight.y = 0;
        if (flight.y > screenY - flight.height)
            flight.y = screenY - flight.height;
        
    }//update

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }//if
    }//draw

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//sleep

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2f) {
                    flight.isGoingUp = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoingUp = false;
                break;
        }
        return true;
    }
}//GameView
