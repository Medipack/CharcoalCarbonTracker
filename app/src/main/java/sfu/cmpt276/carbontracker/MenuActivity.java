package sfu.cmpt276.carbontracker;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import sfu.cmpt276.carbontracker.MenuActivity;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setupAddJourney();
        setupViewJourney();

        setupTemporary();
    }



    private void setupAddJourney() {
        Button addJourney = (Button) findViewById(R.id.addJourney);
        addJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CarsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupViewJourney() {
        Button viewJourney = (Button) findViewById(R.id.viewFootprint);
        viewJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog viewDialog = new Dialog(MenuActivity.this);
                viewDialog.setContentView(R.layout.foot_print_layout);
                viewDialog.show();
            }
        });
    }
    //set up temporary path to route list
    private void setupTemporary() {
        Button temp = (Button) findViewById(R.id.temp);
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, RouteActivity.class);
                startActivity(intent);
            }
        });
    }



}
