package sfu.cmpt276.carbontracker.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;

/*activity to allow user to choose graph to be shown*/
public class GraphActivity extends AppCompatActivity {

    private static final int DAYS_IN_FOUR_WEEKS = 28;
    private static final int DAYS_IN_YEAR = 365;
    private RadioButton pieGraph;
    private RadioButton barGraph;
    private int year_default;
    private int month_default;
    private int day_default;
    private static final int DIALOG_ID = 0;

    private int check;

    private String str_singleDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        createGraphRadioBtn();
        setupShowGraphBtn();
        showDateDialog();

        final Calendar cal = Calendar.getInstance();
        year_default = cal.get(Calendar.YEAR);
        month_default = cal.get(Calendar.MONTH);
        day_default = cal.get(Calendar.DAY_OF_MONTH);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FullScreencall();
        setupFonts();
    }

    private void setupFonts() {
        TextView carbon = (TextView) findViewById(R.id.graphTitle);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/AlexBook.otf");
        carbon.setTypeface(face);
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

    private void createGraphRadioBtn() {
        pieGraph = new RadioButton(this);
        barGraph = new RadioButton(this);

        RadioGroup group = (RadioGroup)findViewById(R.id.graphRB);
        final String[] choice = getResources().getStringArray(R.array.choose_graph);

        pieGraph.setText(choice[0]);
        barGraph.setText(choice[1]);

        group.addView(pieGraph);
        group.addView(barGraph);
    }

    private void showDateDialog() {
        Button chooseSingleDateButton = (Button)findViewById(R.id.chooseTheDate);
        chooseSingleDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID){
            return new DatePickerDialog(this, startDatePickerListener, year_default, month_default, day_default);
        }
        return null;
    }
    private  DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_default = year;
            month_default = month + 1;
            day_default = dayOfMonth;
            Date singleDate = new Date(year_default - 1900, month_default - 1, day_default);

            @SuppressLint("SimpleDateFormat") DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            str_singleDate = df.format(singleDate);

            if(System.currentTimeMillis() < singleDate.getTime()){
                Toast.makeText(GraphActivity.this, "Date cannot be in future", Toast.LENGTH_SHORT).show();
            }

            else {
                TextView singleDateText = (TextView) findViewById(R.id.chosenDate);
                singleDateText.setText(day_default + "/" + month_default + "/" + year_default);
                check = 10;
            }
        }
    };

    private void setupShowGraphBtn() {
        Button show = (Button) findViewById(R.id.showGraphBtn);
        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pieGraph.isChecked()){
                    //check whether user choose the date
                    if(check == 10){
                        Intent intent = new Intent(GraphActivity.this, SingleDayActivity.class);
                        intent.putExtra("date string", str_singleDate);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(GraphActivity.this, "Please choose the date", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(barGraph.isChecked()){
                    Intent intent = new Intent(GraphActivity.this, MultiDayGraphs.class);
                    intent.putExtra("days", DAYS_IN_FOUR_WEEKS);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
