package com.example.flying2dgame;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {

    public int x, y;
    public Bitmap background;

    public Background(int screenX, int screenY, Resources res) {
        x = y = 0;
        background = BitmapFactory.decodeResource(res, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }//Constructor method
}//Background
