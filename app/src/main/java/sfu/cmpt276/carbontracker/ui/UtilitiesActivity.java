package sfu.cmpt276.carbontracker.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Gas;
import sfu.cmpt276.carbontracker.carbonmodel.GasList;

public class UtilitiesActivity extends AppCompatActivity {
    //double e_amount;
    double g_amount;

    //int e_num;
    int g_num;

    double share;

    long startTime;
    long endTime;
    long oneDay = 1000 * 60 * 60 * 24;
    long period;

    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID = 0;
    static final int TO_DIALOG_ID = 1;

    private GasList myGasList = new GasList();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utilities);

        populateGasList();
        setupGasList();
        showFromDialog();
        showToDialog();
        calculateDate();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);

    }
    private void populateGasList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.gas_item,
                myGasList.getGasDescription());            

        ListView gas_list = (ListView) findViewById(R.id.utilityList);
        gas_list.setAdapter(adapter);
    }

    private void setupGasList() {
        final EditText gasAmount = (EditText)findViewById(R.id.amountEnter);
        final EditText gasNum = (EditText)findViewById(R.id.shareEnter);

        Button gasButton = (Button)findViewById(R.id.saveChange);

        gasButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_gasAmount = gasAmount.getText().toString();
                g_amount = Double.valueOf(str_gasAmount);
                String str_electricityNuM = gasNum.getText().toString();
                g_num = Integer.parseInt(str_electricityNuM);
                share = g_amount/g_num;
                TextView cal = (TextView) findViewById(R.id.cal);
                String str_share = String.valueOf(share);
                cal.setText(str_share);

                Gas updateGas = new Gas(g_amount, g_num);
                myGasList.addGas(updateGas);

                double perDay = g_amount/period;
                TextView per_day = (TextView) findViewById(R.id.perDay);
                String str_per_day = Double.toString(perDay);
                //per_day.setText(str_per_day);
                String test = Long.toString(period);
                per_day.setText(test);

                populateGasList();
                //long test_ = endTime/oneDay - startTime/oneDay;
                Toast.makeText(UtilitiesActivity.this, "" + endTime, Toast.LENGTH_SHORT).show();
                Toast.makeText(UtilitiesActivity.this, "" + startTime, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showFromDialog(){
        Button fromBtn = (Button) findViewById(R.id.from);

        fromBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_ID);
            }
        });
    }

    private void showToDialog() {
        Button fromBtn = (Button) findViewById(R.id.to);

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
            TextView startDate = (TextView) findViewById(R.id.startDate);
            startDate.setText(day_x + "/" + month_x + "/" + year_x);
        }
    };

    private  DatePickerDialog.OnDateSetListener endDatePickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_y = year;
            month_y = month + 1;
            day_y = dayOfMonth;

            if(year_x > year_y || month_x > month_y || day_x >= day_y){
                Toast.makeText(UtilitiesActivity.this, "Please pick up a right date", Toast.LENGTH_SHORT).show();
            }
            else {
                TextView endDate = (TextView) findViewById(R.id.end);
                endDate.setText(day_y + "/" + month_y  + "/" + year_y);
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calculateDate() {
        /*
        Calendar startDay = Calendar.getInstance();
        startDay.set(Calendar.DAY_OF_MONTH, day_x);
        startDay.set(Calendar.MONTH, month_x);
        startDay.set(Calendar.YEAR, year_x);

        Calendar endDay = Calendar.getInstance();
        endDay.set(Calendar.DAY_OF_MONTH, day_y);
        endDay.set(Calendar.MONTH, month_y);
        endDay.set(Calendar.YEAR, year_y);
        */

        Date date1 = new Date(year_x, month_x, day_x);
        Date date2 = new Date(year_y, month_y, day_y);


        startTime = date1.getTime();
        endTime = date2.getTime();
        period = (endTime - startTime)/oneDay;


        //startTime = startDay.getTimeInMillis();
        //endTime = endDay.getTimeInMillis();
        //oneDay = (endTime - startTime)/1000;
        //period = oneDay/60/60/24;
    }


}
