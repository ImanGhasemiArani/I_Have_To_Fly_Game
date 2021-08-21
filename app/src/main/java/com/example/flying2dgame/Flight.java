package com.example.flying2dgame;

import static com.example.flying2dgame.GameView.screenRatioX;
import static com.example.flying2dgame.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Flight {

    public int x, y;
    public final int width, height;
    public boolean isGoingUp = false;
    private int wingCounter = 0;
    private int shootCounter = 1;
    public int toShoot = 0;
    private Bitmap flight1, flight2, shoot1, shoot2, shoot3, shoot4, shoot5;
    private final GameView gameView;


    public Flight(GameView gameView, int screenY, Resources res) {
        this.gameView = gameView;
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

        width = (int) (flight1.getWidth() / 4 * screenRatioX);
        height = (int) (flight2.getHeight() / 4 * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        this.x = (int) (64 * screenRatioX);
        this.y = screenY / 2;

        shoot1 = BitmapFactory.decodeResource(res, R.drawable.shoot1);
        shoot2 = BitmapFactory.decodeResource(res, R.drawable.shoot2);
        shoot3 = BitmapFactory.decodeResource(res, R.drawable.shoot3);
        shoot4 = BitmapFactory.decodeResource(res, R.drawable.shoot4);
        shoot5 = BitmapFactory.decodeResource(res, R.drawable.shoot5);

        shoot1 = Bitmap.createScaledBitmap(shoot1, width, height, false);
        shoot2 = Bitmap.createScaledBitmap(shoot2, width, height, false);
        shoot3 = Bitmap.createScaledBitmap(shoot3, width, height, false);
        shoot4 = Bitmap.createScaledBitmap(shoot4, width, height, false);
        shoot5 = Bitmap.createScaledBitmap(shoot5, width, height, false);

    }//Constructor method

    public Bitmap getFlight() {

        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++;
                return shoot1;
            }//if
            if (shootCounter == 2) {
                shootCounter++;
                return shoot2;
            }//if
            if (shootCounter == 3) {
                shootCounter++;
                return shoot3;
            }//if
            if (shootCounter == 4) {
                shootCounter++;
                return shoot4;
            }//if

            shootCounter = 1;
            toShoot--;
            gameView.newBullet();
            return shoot5;
        }//if

        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        }//if
        wingCounter--;
        return flight2;

    }//getFlight
}//Flight
