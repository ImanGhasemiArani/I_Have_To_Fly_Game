package com.example.flying2dgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private GameActivity activity;
    private Thread gameThread;
    private boolean isPlaying, isGameOver = false;
    public static float screenRatioX, screenRatioY;
    private int screenX, screenY, score = 0;
    private Random random;
    private Paint paint;
    private Background background1, background2;
    private Flight flight;
    private ArrayList<Bullet> bullets;
    private Bird[] birds;
    private SharedPreferences prefs;
    private SoundPool soundPool;
    private int sound;

    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);
        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        sound = soundPool.load(activity, R.raw.shoot, 1);

        random = new Random();
        this.screenX = screenX;
        this.screenY = screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());
        background2.x = screenX;
        screenRatioX = 1920f / screenX;
        screenRatioY = 1080f / screenY;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        flight = new Flight(this, screenY, getResources());

        bullets = new ArrayList<>();

        birds = new Bird[4];
        for (int i = 0; i < birds.length; i++)
            birds[i] = new Bird(getResources());



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

        //add some bullets to trash for removing those from bullets arraylist
        ArrayList<Bullet> trashTemp = new ArrayList<>();
        //update bullets
        for (Bullet bullet : bullets) {
            if (bullet.x > screenX)
                trashTemp.add(bullet);

            bullet.x +=  50 * screenRatioX;

            for (Bird bird : birds) {
                if (Rect.intersects(bird.getCollisionShape(), bullet.getCollisionShape())) {
                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;
                }
            }

        }//if
        //removing trash from bullets arraylist
        for (Bullet bullet : trashTemp)
            bullets.remove(bullet);

        for (Bird bird : birds) {
            bird.x -= bird.speed;
            if (bird.x + bird.width < 0) {
                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }
                bird.speed = random.nextInt((int) (30 * screenRatioX));
                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);

                bird.x = screenX;
                bird.y = random.nextInt(screenY - bird.height);
                bird.wasShot = false;
            }
            if (Rect.intersects(bird.getCollisionShape(), flight.getCollisionShape())) {
                isGameOver = true;
                return;
            }

        }
    }//update

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();

            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            for (Bird bird: birds)
                canvas.drawBitmap(bird.getBird(), bird.x, bird.y, paint);

            canvas.drawText(score + "" , screenX / 2f, 164, paint);

            if (isGameOver) {
                isPlaying = false;
                canvas.drawBitmap(flight.getDead(), flight.x, flight.y, paint);
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExiting();
                return;
            }//if

            canvas.drawBitmap(flight.getFlight(), flight.x, flight.y, paint);

            for (Bullet bullet : bullets )
                canvas.drawBitmap(bullet.bullet, bullet.x, bullet.y, paint);

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
                if (event.getX() > screenX / 2f) {
                    flight.toShoot++;
                }
                break;
        }
        return true;
    }

    public void newBullet() {

        if (!prefs.getBoolean("isMute", false)) {
            soundPool.play(sound,1 , 1, 0, 0, 1);
        }
        Bullet bullet = new Bullet(getResources());
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + flight.height / 2;
        bullets.add(bullet);
    }//newBullet

    private void saveIfHighScore() {

        if (prefs.getInt("highScore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highScore", score);
            editor.apply();
        }//if
    }//saveIfHighScore

    private void waitBeforeExiting() {

        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }//waitBeforeExiting
}//GameView
