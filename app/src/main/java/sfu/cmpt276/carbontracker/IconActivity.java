package sfu.cmpt276.carbontracker;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Icon;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;

import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Vehicle;
import sfu.cmpt276.carbontracker.ui.NewVehicleFragment;
import sfu.cmpt276.carbontracker.ui.RouteActivity;
import sfu.cmpt276.carbontracker.ui.TransportationModeActivity;
import sfu.cmpt276.carbontracker.ui.database.Database;

public class IconActivity extends AppCompatActivity {
    Button[] buttonArray = new Button[9];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        loadIcons();
        setupOkButton();
        setupIconButtonclicks();
        loadCurrentIcon();
    }



    private void loadCurrentIcon() {
        Button currentIcon= (Button)findViewById(R.id.currentIconImg);
        int iconID = User.getInstance().getCurrentJourney().getVehicle().getIconID();
        TypedArray icons = getResources().obtainTypedArray(R.array.iconArray);
        currentIcon.setBackground(icons.getDrawable(iconID));
    }

    private void setupIconButtonclicks() {
        for(int i  = 0; i < buttonArray.length; i++)
        {
            final int i_final = i;
            buttonArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button currentIcon= (Button)findViewById(R.id.currentIconImg);
                    TypedArray icons = getResources().obtainTypedArray(R.array.iconArray);
                    currentIcon.setBackground(icons.getDrawable(i_final));
                    Vehicle vehicle = User.getInstance().getCurrentJourney().getVehicle();
                    vehicle.setIconID(i_final);
                    Database.getDB().updateVehicle(vehicle);

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        formatButtons();
        loadCurrentIcon();
    }

    private void formatButtons() {

        for(Button button : buttonArray) {
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = 300;
            params.height = 300;
            button.setLayoutParams(params);
        }
    }

    private void setupOkButton() {
        Button okBtn = (Button)findViewById(R.id.okIconBtn);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int caller = getIntent().getIntExtra("caller" , -1);
                if(caller!=0) {
                    Intent intent = new Intent(IconActivity.this, RouteActivity.class);
                    startActivityForResult(intent, 0);
                }
                finish();
            }
        });
    }

    private void loadIcons() {
        GridLayout iconGrid = (GridLayout)findViewById(R.id.iconGrid);
        TypedArray icons = getResources().obtainTypedArray(R.array.iconArray);

        for(int i = 0 ; i < icons.length(); i++)
        {
            Button newButton = new Button(this);
            newButton.setBackground(icons.getDrawable(i));
            iconGrid.addView(newButton);
            buttonArray[i] = newButton;
        }
        icons.recycle();
    }
}
