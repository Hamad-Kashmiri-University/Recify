package com.example.recify;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMER = 3000;

    //vars
    Animation topAnimHome, bottomAnimHome;
    ImageView topImage;
    ImageView bottomImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // gets rid of status bar
        setContentView(R.layout.activity_main);

        // home animation
        topAnimHome = AnimationUtils.loadAnimation(this, R.anim.top_anim_home);
        bottomAnimHome = AnimationUtils.loadAnimation(this, R.anim.bottom_anim_home);

        // hooks
        topImage = findViewById(R.id.topimage);
        bottomImage = findViewById(R.id.bottomimage);

        topImage.setAnimation(topAnimHome);
        bottomImage.setAnimation(bottomAnimHome);

        // transition to menu page, call login when defined time is reached
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                //animation inspired from coding with tea
                Pair[] pairs = new Pair[2];
                pairs[0] = new Pair<View,String>(topImage,"logotop");
                pairs[1] = new Pair<View,String>(bottomImage,"logobottom");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(intent, options.toBundle());
                }

                //finish();
            }
        }, SPLASH_SCREEN_TIMER);
    }
}
