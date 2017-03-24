package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import sfu.cmpt276.carbontracker.R;

/*
Main activity splash screen, displays welcome image and proceeds to main menu
 */
public class StartAnimationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_animation);
        setupCar();

        setupWelcome();
        int welcomeTime = 6000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(StartAnimationActivity.this, MainMenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, welcomeTime);
    }

    private void setupWelcome() {
        TextView welcome = (TextView) findViewById(R.id.NameTitle);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Cache.ttf");
        welcome.setTypeface(face);


    }

    private void setupCar() {
        ImageView car1 =(ImageView) findViewById(R.id.carOne);
        car1.animate().translationX(car1.getTranslationX()+900).setDuration(6500);
        ImageView car2 =(ImageView) findViewById(R.id.carTwo);
        car2.animate().translationX(car1.getTranslationX()+1600).setDuration(6500);
    }
}