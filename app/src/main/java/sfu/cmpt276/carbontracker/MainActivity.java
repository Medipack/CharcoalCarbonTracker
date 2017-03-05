package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static int welcomeTime = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupCar();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, welcomeTime);
    }

    private void setupCar() {
        ImageView car1 =(ImageView) findViewById(R.id.carOne);
        car1.animate().translationX(car1.getTranslationX()+900).setDuration(6500);
        ImageView car2 =(ImageView) findViewById(R.id.carTwo);
        car2.animate().translationX(car1.getTranslationX()+1600).setDuration(6500);
    }
}
