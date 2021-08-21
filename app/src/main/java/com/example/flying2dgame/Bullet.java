package com.example.flying2dgame;

import static com.example.flying2dgame.GameView.screenRatioX;
import static com.example.flying2dgame.GameView.screenRatioY;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Bullet {

    public int x, y;
    private final int width, height;
    public Bitmap bullet;

    public Bullet(Resources res) {
        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet);
        width = (int) (bullet.getWidth() / 4 * screenRatioX);
        height = (int) (bullet.getHeight() / 4 * screenRatioY);
        bullet = Bitmap.createScaledBitmap(bullet, width, height, false);
    }
}
