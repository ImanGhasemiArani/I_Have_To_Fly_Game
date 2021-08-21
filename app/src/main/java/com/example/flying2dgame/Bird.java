package com.example.flying2dgame;

import static com.example.flying2dgame.GameView.screenRatioX;
import static com.example.flying2dgame.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Bird {

    public int x = 0, y, width, height, speed = 20;
    private int wingCounter = 1;
    public boolean wasShot = true;
    public Bitmap bird1, bird2, bird3, bird4;

    public Bird(Resources res) {
        bird1 = BitmapFactory.decodeResource(res, R.drawable.bird1);
        bird2 = BitmapFactory.decodeResource(res, R.drawable.bird2);
        bird3 = BitmapFactory.decodeResource(res, R.drawable.bird3);
        bird4 = BitmapFactory.decodeResource(res, R.drawable.bird4);

        width = (int) (bird1.getWidth() / 6 * screenRatioX);
        height = (int) (bird1.getHeight() / 6 * screenRatioY);

        bird1 = Bitmap.createScaledBitmap(bird1, width, height, false);
        bird2 = Bitmap.createScaledBitmap(bird2, width, height, false);
        bird3 = Bitmap.createScaledBitmap(bird3, width, height, false);
        bird4 = Bitmap.createScaledBitmap(bird4, width, height, false);

        y = -height;
    }//Constructor method

    public Bitmap getBird() {
        if (wingCounter == 1) {
            wingCounter++;
            return bird1;
        }//if
        if (wingCounter == 2) {
            wingCounter++;
            return bird2;
        }//if
        if (wingCounter == 3) {
            wingCounter++;
            return bird3;
        }//if

        wingCounter = 1;
        return bird4;
    }//getBird

    public Rect getCollisionShape() {
        return new Rect(x, y, x + width, y + height);
    }//getCollisionShape

}//Bird
