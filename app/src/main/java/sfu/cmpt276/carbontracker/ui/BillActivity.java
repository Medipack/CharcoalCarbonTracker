package sfu.cmpt276.carbontracker.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class BillActivity extends AppCompatActivity {

    long startTime;
    long endTime;
    long oneDay = 1000 * 60 * 60 * 24;
    long period;
    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID = 0;
    static final int TO_DIALOG_ID = 1;
    int utilityChosen;
    double amount;
    int people;
    double currentAvg;
    double previousAvg;
    Date startDate;
    Date endDate;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        showFromDialog();
        showToDialog();


        createRadioButton();
        setupSaveButton();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void createRadioButton() {
        RadioGroup group = (RadioGroup)findViewById(R.id.utilityGroup);
        final String[] choice = getResources().getStringArray(R.array.choose_utility);

        for(int i = 0; i < choice.length; i++){
            final String utility1 = choice[i];

            RadioButton button = new RadioButton(this);
            button.setText(utility1 + "");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(BillActivity.this, "You clicked " + utility1, Toast.LENGTH_SHORT).show();
                    if(utility1.equals(choice[0])){
                        utilityChosen = 0; //user choose natural gas
                    }
                    else{
                        utilityChosen = 1; //user choose electricity
                    }
                }
            });
            group.addView(button);
        }
    }



    private void setupSaveButton() {
        final EditText amountInput = (EditText) findViewById(R.id.amountInput);
        /*
        final EditText peopleInput = (EditText) findViewById(R.id.peopleInput);
        final EditText currentAvgInput = (EditText) findViewById(R.id.currentAvgInput);
        final EditText previousAvgInput = (EditText) findViewById(R.id.previousAvgInput);


        final String str_amount = amountInput.getText().toString();
        amount = Double.valueOf(str_amount);

        String str_people = peopleInput.getText().toString();
        people = Integer.getInteger(str_people);
        String str_currentAvg = currentAvgInput.getText().toString();
        currentAvg = Double.valueOf(str_currentAvg);
        String str_previousAvg = previousAvgInput.getText().toString();
        previousAvg = Double.valueOf(str_previousAvg);
        */

        //if (utilityChosen == 0) {
        //    Utility test = new Utility("gas", startDate, endDate, amount, people, 5, currentAvg, previousAvg);
        //    Toast.makeText(this, "" + test, Toast.LENGTH_SHORT).show();
        //}

        //Toast.makeText(this, "" + amount, Toast.LENGTH_SHORT).show();
        Button save = (Button)findViewById(R.id.utilitySaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BillActivity.this, "" + startDate, Toast.LENGTH_SHORT).show();
                Toast.makeText(BillActivity.this, "" + endDate, Toast.LENGTH_SHORT).show();
                Toast.makeText(BillActivity.this, "" + period, Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showFromDialog() {
        Button fromBtn = (Button) findViewById(R.id.startBtn);
        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    private void showToDialog() {
        Button fromBtn = (Button) findViewById(R.id.endBtn);
        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TO_DIALOG_ID);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        if(id == DIALOG_ID){
            return new DatePickerDialog(this, startDatePickerListener, year_x, month_x, day_x);
        }
        else if(id == TO_DIALOG_ID){
            return new DatePickerDialog(this, endDatePickerListener, year_y, month_y, day_y);
        }
        return null;
    }
    private  DatePickerDialog.OnDateSetListener startDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            startDate = new Date(year_x - 1900, month_x - 1, day_x);
            TextView startDate = (TextView) findViewById(R.id.startDateText);
            startDate.setText(day_x + "/" + month_x + "/" + year_x);
        }
    };
    private  DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_y = year;
            month_y = month + 1;
            day_y = dayOfMonth;
            endDate = new Date(year_y - 1900, month_y - 1, day_y);
            long temp = endDate.getTime() - startDate.getTime();
            period = temp/oneDay + 1;
            if(year_x > year_y || month_x > month_y || day_x >= day_y){
                Toast.makeText(BillActivity.this, "Please pick up a right date", Toast.LENGTH_SHORT).show();
            }
            else {
                TextView endDate = (TextView) findViewById(R.id.endDateText);
                endDate.setText(day_y + "/" + month_y  + "/" + year_y);
            }
        }
    };

}
