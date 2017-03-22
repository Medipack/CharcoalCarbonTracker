package sfu.cmpt276.carbontracker.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
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

public class GraphActivity extends AppCompatActivity {

    public static final int DAYS_IN_FOUR_WEEKS = 28;
    public static final int DAYS_IN_YEAR = 365;
    private RadioButton singleDay;
    private RadioButton last28Days;
    private RadioButton last365Days;
    int year_default, month_default, day_default;
    static final int DIALOG_ID = 0;

    int check;

    Date singleDate;
    String str_singleDate;

    TextView singleDateText;

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
    }

    private void createGraphRadioBtn() {
        singleDay = new RadioButton(this);
        last28Days = new RadioButton(this);
        last365Days = new RadioButton(this);

        RadioGroup group = (RadioGroup)findViewById(R.id.graphRB);
        final String[] choice = getResources().getStringArray(R.array.choose_graph);

        singleDay.setText(choice[0]);
        last28Days.setText(choice[1]);
        last365Days.setText(choice[2]);

        group.addView(singleDay);
        group.addView(last28Days);
        group.addView(last365Days);
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
            singleDate = new Date(year_default - 1900, month_default - 1, day_default);

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            str_singleDate = df.format(singleDate);

            if(System.currentTimeMillis() < singleDate.getTime()){
                Toast.makeText(GraphActivity.this, "Date cannot be in future", Toast.LENGTH_SHORT).show();
            }

            else {
                singleDateText = (TextView) findViewById(R.id.chosenDate);
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
                if(singleDay.isChecked()){
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
                else if(last28Days.isChecked()){
                    Toast.makeText(GraphActivity.this, "28", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GraphActivity.this, MultiDayGraphs.class);
                    intent.putExtra("days", DAYS_IN_FOUR_WEEKS);
                    startActivity(intent);
                }
                else if(last365Days.isChecked()){
                    Toast.makeText(GraphActivity.this, "365", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(GraphActivity.this, MultiDayGraphs.class);
                    intent.putExtra("days", DAYS_IN_YEAR);
                    startActivity(intent);
                }
            }
        });
    }
}
