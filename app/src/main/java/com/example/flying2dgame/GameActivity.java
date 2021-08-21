package com.example.flying2dgame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.WindowManager;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        gameView = new GameView(this, size.x, size.y);

        setContentView(gameView);

    }//onCreate

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }//onPause

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }//onResume
}//GameActivity