package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
        FullScreencall();
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT < 19){
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else {
            //for higher api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void setupWelcome() {
        TextView carbon = (TextView) findViewById(R.id.NameTitle);
        Typeface face_carbon = Typeface.createFromAsset(getAssets(),"fonts/Cache.ttf");
        carbon.setTypeface(face_carbon);

        TextView tracker = (TextView) findViewById(R.id.NameTitleTracker);
        Typeface face_tracker = Typeface.createFromAsset(getAssets(),"fonts/Cache.ttf");
        tracker.setTypeface(face_tracker);

        TextView charcoal = (TextView) findViewById(R.id.groupNameText);
        Typeface face_charcoal = Typeface.createFromAsset(getAssets(),"fonts/Cache.ttf");
        charcoal.setTypeface(face_charcoal);
    }

    private void setupCar() {
        ImageView car1 =(ImageView) findViewById(R.id.carOne);
        car1.animate().translationX(car1.getTranslationX()+900).setDuration(6500);
        ImageView car2 =(ImageView) findViewById(R.id.carTwo);
        car2.animate().translationX(car1.getTranslationX()+1600).setDuration(6500);
    }
}