package sfu.cmpt276.carbontracker.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

public class BillActivity extends AppCompatActivity {

    long oneDay = 1000 * 60 * 60 * 24;
    long period;
    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID = 0;
    static final int TO_DIALOG_ID = 1;
    int utilityChosen;
    Date startDate;
    Date endDate;

    private EditText amountInput;
    private EditText peopleInput;
    private EditText currentAvgInput;
    private EditText previousAvgInput;

    String str_startDate;
    String str_endDate;

    String tempChosen;
    String tempPeriod;
    String tempType;

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
                    if(utility1.equals(choice[0])){
                        utilityChosen = 0; //user choose natural gas
                        tempChosen = Integer.toString(utilityChosen);
                    }
                    else{
                        utilityChosen = 1; //user choose electricity
                        tempChosen = Integer.toString(utilityChosen);
                    }
                }
            });
            group.addView(button);
        }
    }



    private void setupSaveButton() {
        Button save = (Button)findViewById(R.id.utilitySaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountInput = (EditText) findViewById(R.id.amountInput);
                peopleInput = (EditText) findViewById(R.id.peopleInput);
                currentAvgInput = (EditText) findViewById(R.id.currentAvgInput);
                previousAvgInput = (EditText) findViewById(R.id.previousAvgInput);

                String str_amount = amountInput.getText().toString();
                String str_people = peopleInput.getText().toString();
                String str_currentAvg = currentAvgInput.getText().toString();
                String str_previousAvg = previousAvgInput.getText().toString();

                if(utilityChosen == 0){
                    tempType = "gas";
                    Toast.makeText(BillActivity.this, "" + tempType, Toast.LENGTH_SHORT).show();
                }
                else{
                    tempType = "electricity";
                    Toast.makeText(BillActivity.this, "" + tempType, Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(BillActivity.this, UtilityActivity.class);
                intent.putExtra("type of utility", tempType);
                intent.putExtra("start date", str_startDate);
                intent.putExtra("end date", str_endDate);
                intent.putExtra("amount", str_amount);
                intent.putExtra("people", str_people);
                intent.putExtra("period", tempPeriod);
                intent.putExtra("current avg", str_currentAvg);
                intent.putExtra("previous avg", str_previousAvg);

                setResult(Activity.RESULT_OK, intent);
                finish();

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

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            str_startDate = df.format(startDate);

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

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            str_endDate = df.format(endDate);

            period = temp/oneDay + 1;
            tempPeriod = Long.toString(period);
            if(startDate.getTime() >= endDate.getTime()){
                Toast.makeText(BillActivity.this, "End date cannot before the start date", Toast.LENGTH_SHORT).show();
            }
            else if(System.currentTimeMillis() <= startDate.getTime()){
                Toast.makeText(BillActivity.this, "Start date cannot be today or future", Toast.LENGTH_SHORT).show();
            }
            else if(System.currentTimeMillis() < endDate.getTime()){
                Toast.makeText(BillActivity.this, "End date cannot be future", Toast.LENGTH_SHORT).show();
            }
            else {
                TextView endDate = (TextView) findViewById(R.id.endDateText);
                endDate.setText(day_y + "/" + month_y  + "/" + year_y);
            }
        }
    };


    public static String getTypeName(Intent intent){
        return intent.getStringExtra("type of utility");
    }

    public static String getStartDate(Intent intent){
        return intent.getStringExtra("start date");
    }

    public static String getEndDate(Intent intent){
        return intent.getStringExtra("end date");
    }

    public static String getUsed(Intent intent){
        return intent.getStringExtra("amount");
    }

    public static String getPeople(Intent intent){
        return intent.getStringExtra("people");
    }

    public static String getPeriod(Intent intent){
        return intent.getStringExtra("period");
    }

    public static String getCurrentAvg(Intent intent){
        return intent.getStringExtra("current avg");
    }

    public static String getPreviousAvg(Intent intent){
        return intent.getStringExtra("previous avg");
    }

    public static Intent makeIntent(Context context){
        return new Intent(context, BillActivity.class);
    }
}
