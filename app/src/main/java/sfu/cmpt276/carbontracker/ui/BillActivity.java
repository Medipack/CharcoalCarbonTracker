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

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.carbonmodel.UtilityList;

public class BillActivity extends AppCompatActivity {

    long oneDay = 1000 * 60 * 60 * 24;
    long period;
    int year_x, month_x, day_x;
    int year_y, month_y, day_y;
    static final int DIALOG_ID = 0;
    static final int TO_DIALOG_ID = 1;

    Date startDate;
    Date endDate;
    int tempMode;

    int editStartYear, editStartMonth, editStartDay;
    int editEndYear, editEndMonth, editEndDay;


    private EditText amountInput;
    private EditText peopleInput;
    private RadioButton gasRb;
    private RadioButton electricityRb;

    String str_startDate;
    String str_endDate;

    TextView startDateText;
    TextView endDateText;

    String tempPeriod;
    int position;
    String str_amount;
    String str_people;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        showFromDialog();
        showToDialog();

        createRadioButton();
        setupSaveButton();
        setupDeleteButton();

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        year_y = cal.get(Calendar.YEAR);
        month_y = cal.get(Calendar.MONTH);
        day_y = cal.get(Calendar.DAY_OF_MONTH);
        Intent intent = getIntent();
        if(intent.getExtras() != null) {
            position = intent.getIntExtra("pos", 0);
            populateBill(position);
        }
    }

    private void createRadioButton() {
        gasRb = new RadioButton(this);
        electricityRb = new RadioButton(this);

        RadioGroup group = (RadioGroup)findViewById(R.id.utilityGroup);
        final String[] choice = getResources().getStringArray(R.array.choose_utility);

        gasRb.setText(choice[0]);
        electricityRb.setText(choice[1]);

        group.addView(gasRb);
        group.addView(electricityRb);

    }
    //set up save btn
    private void setupSaveButton() {
        Button save = (Button)findViewById(R.id.utilitySaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility newUtility = new Utility();
                amountInput = (EditText) findViewById(R.id.amountInput);
                peopleInput = (EditText) findViewById(R.id.peopleInput);

                //input check
                String startCheck = startDateText.getText().toString();
                String endCheck = endDateText.getText().toString();
                if(startCheck.startsWith("x") || endCheck.startsWith("x")){
                    Toast.makeText(BillActivity.this, "Please choose the date", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (amountInput.length() == 0) {
                        Toast.makeText(BillActivity.this, "Please enter the amount", Toast.LENGTH_SHORT).show();
                    } else if (peopleInput.length() == 0) {
                        Toast.makeText(BillActivity.this, "Please enter the number of people in the house", Toast.LENGTH_SHORT).show();
                    } else if (peopleInput.length() == 0) {
                        Toast.makeText(BillActivity.this, "Please enter the number of people in the house", Toast.LENGTH_SHORT).show();
                    } else {
                        str_amount = amountInput.getText().toString();
                        str_people = peopleInput.getText().toString();

                        if (gasRb.isChecked()) {
                            newUtility.setUtility_type(Utility.GAS_NAME);
                            newUtility.setNaturalGasUsed(Double.parseDouble(str_amount));
                            newUtility.setNumberOfPeople(Integer.parseInt(str_people));
                            newUtility.setStartDate(startDate);
                            newUtility.setEndDate(endDate);
                            newUtility.setDaysInPeriod(Integer.parseInt(tempPeriod));
                            Intent intent = getIntent();
                            tempMode = intent.getIntExtra("mode", 0);
                            //edit mode
                            if (tempMode == 10) {
                                User.getInstance().EditUtilityIntoUtilityList(position, newUtility);
                            } else {
                                UtilityList tempList = User.getInstance().getUtilityList();
                                tempList.addUtility(newUtility);
                            }
                            finish();
                        } else if (electricityRb.isChecked()) {
                            newUtility.setUtility_type(Utility.ELECTRICITY_NAME);
                            newUtility.setElectricUsed(Double.parseDouble(str_amount));

                            newUtility.setNumberOfPeople(Integer.parseInt(str_people));
                            newUtility.setStartDate(startDate);
                            newUtility.setEndDate(endDate);
                            newUtility.setDaysInPeriod(Integer.parseInt(tempPeriod));
                            Intent intent = getIntent();
                            tempMode = intent.getIntExtra("mode", 0);
                            //edit mode
                            if (tempMode == 10) {
                                User.getInstance().EditUtilityIntoUtilityList(position, newUtility);
                            } else {
                                UtilityList tempList = User.getInstance().getUtilityList();
                                tempList.addUtility(newUtility);
                            }
                            finish();
                        } else {
                            Toast.makeText(BillActivity.this, "Please choose the utility type", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
    }

    private void setupDeleteButton() {
        Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.getInstance().getUtilityList().removeUtility(position);
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

            startDateText = (TextView) findViewById(R.id.startDateText);
            startDateText.setText(day_x + "/" + month_x + "/" + year_x);

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
                Toast.makeText(BillActivity.this, "End date cannot be before the start date", Toast.LENGTH_SHORT).show();
            }
            else if(System.currentTimeMillis() <= startDate.getTime()){
                Toast.makeText(BillActivity.this, "Start date cannot be today or future", Toast.LENGTH_SHORT).show();
            }
            else if(System.currentTimeMillis() < endDate.getTime()){
                Toast.makeText(BillActivity.this, "End date cannot be future", Toast.LENGTH_SHORT).show();
            }
            else {
                endDateText = (TextView) findViewById(R.id.endDateText);
                endDateText.setText(day_y + "/" + month_y  + "/" + year_y);
            }
        }
    };


    public static Intent makeIntent(Context context){
        return new Intent(context, BillActivity.class);
    }

    public void populateBill(int position)
    {
        Utility utility = User.getInstance().getUtilityList().getUtility(position);
        amountInput = (EditText) findViewById(R.id.amountInput);
        peopleInput = (EditText) findViewById(R.id.peopleInput);

        startDateText = (TextView) findViewById(R.id.startDateText);
        endDateText = (TextView) findViewById(R.id.endDateText);

        long startTimeInMillis = utility.getStartDate().getTime();
        long endTimeInMillis = utility.getStartDate().getTime();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis (startTimeInMillis);
        editStartYear = calendar.get(Calendar.YEAR);
        editStartMonth = calendar.get(Calendar.MONTH) + 1;
        editStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDateText.setText(editStartDay + "/" + editStartMonth  + "/" + editStartYear);

        calendar.setTimeInMillis (endTimeInMillis);
        editEndYear = calendar.get(Calendar.YEAR);
        editEndMonth = calendar.get(Calendar.MONTH) + 1;
        editEndDay = calendar.get(Calendar.DAY_OF_MONTH);
        endDateText.setText(editEndDay + "/" + editEndMonth  + "/" + editEndYear);

        peopleInput.setText(String.valueOf(utility.getNumberOfPeople()));
        if(utility.getUtility_type().equals(Utility.GAS_NAME))
        {
            gasRb.setChecked(true);
            amountInput.setText(String.valueOf(utility.getNaturalGasUsed()));

        }
        else if(utility.getUtility_type().equals(Utility.ELECTRICITY_NAME))
        {
            electricityRb.setChecked(true);
            amountInput.setText(String.valueOf(utility.getElectricUsed()));
        }
    }
}
