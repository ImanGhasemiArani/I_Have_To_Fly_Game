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
    private Bitmap flight1, flight2;

    public Flight(int screenY, Resources res) {
        flight1 = BitmapFactory.decodeResource(res, R.drawable.fly1);
        flight2 = BitmapFactory.decodeResource(res, R.drawable.fly2);

        width = (int) (flight1.getWidth() / 4 * screenRatioX);
        height = (int) (flight2.getHeight() / 4 * screenRatioY);

        flight1 = Bitmap.createScaledBitmap(flight1, width, height, false);
        flight2 = Bitmap.createScaledBitmap(flight2, width, height, false);

        this.x = (int) (64 * screenRatioX);
        this.y = screenY / 2;

    }//Constructor method

    public Bitmap getFlight() {
        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        }//if

        else {
            wingCounter--;
            return flight2;
        }//else
    }//getFlight
}//Flight
