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

import sfu.cmpt276.carbontracker.ui.RouteActivity;
import sfu.cmpt276.carbontracker.ui.TransportationModeActivity;

public class IconActivity extends AppCompatActivity {
    Button[] buttonArray = new Button[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);

        loadIcons();
        setupOkButton();
        setupIconButtonclicks();
    }

    private void setupIconButtonclicks() {
        for(Button button : buttonArray)
        {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        formatButtons();
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
                Intent intent = new Intent(IconActivity.this, RouteActivity.class);
                startActivityForResult(intent, 0);
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
