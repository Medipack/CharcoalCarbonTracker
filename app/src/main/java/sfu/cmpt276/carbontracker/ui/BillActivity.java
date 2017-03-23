package sfu.cmpt276.carbontracker.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;
import sfu.cmpt276.carbontracker.ui.database.UtilityDataSource;
/*Activity to display bills for user input*/
public class BillActivity extends AppCompatActivity {

    private final String TAG = "BillActivity";

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

    private boolean startDateSet = false;
    private boolean endDateSet = false;

    private EditText amountInput;
    private EditText peopleInput;
    private EditText currentAvgInput;
    private EditText previousAvgInput;
    private RadioButton gasRb;
    private RadioButton electricityRb;

    String str_startDate;
    String str_endDate;

    TextView startDateText;
    TextView endDateText;

    String tempPeriod;
    int position;

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

    private void setupSaveButton() {
        Button save = (Button)findViewById(R.id.utilitySaveBtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility newUtility = new Utility();

                amountInput = (EditText) findViewById(R.id.amountInput);
                String str_amount = amountInput.getText().toString();
                if(str_amount.length() == 0) {
                    showErrorToast("Please enter the amount used");
                    return;
                }

                peopleInput = (EditText) findViewById(R.id.peopleInput);
                String str_people = peopleInput.getText().toString();
                if(str_people.length() == 0) {
                    showErrorToast("Please enter the number of people");
                    return;
                }

                currentAvgInput = (EditText) findViewById(R.id.currentAvgInput);
                String str_currentAvg = currentAvgInput.getText().toString();
                if(str_currentAvg.length() == 0) {
                    showErrorToast("Please enter the current average use");
                    return;
                }

                previousAvgInput = (EditText) findViewById(R.id.previousAvgInput);
                String str_previousAvg = previousAvgInput.getText().toString();
                if(str_previousAvg.length() == 0) {
                    showErrorToast("Please enter the previous average use");
                    return;
                }

                if(gasRb.isChecked()){
                    newUtility.setUtility_type(Utility.GAS_NAME);
                    newUtility.setNaturalGasUsed(Double.parseDouble(str_amount));
                    newUtility.setAverageGJCurrent(Double.parseDouble(str_currentAvg));
                    newUtility.setAverageGJPrevious(Double.parseDouble(str_previousAvg));
                }
                else if(electricityRb.isChecked()){
                    newUtility.setUtility_type(Utility.ELECTRICITY_NAME);
                    newUtility.setElectricUsed(Double.parseDouble(str_amount));
                    newUtility.setAverageKWhCurrent(Double.parseDouble(str_currentAvg));
                    newUtility.setAverageKWhPrevious(Double.parseDouble(str_previousAvg));
                } else {
                    Toast.makeText(BillActivity.this, "Select Gas or Electricity", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!startDateSet) {
                    showErrorToast("Please enter a start date");
                    return;
                }

                if(!endDateSet) {
                    showErrorToast("Please enter an end date");
                    return;
                }

                newUtility.setNumberOfPeople(Integer.parseInt(str_people));
                newUtility.setStartDate(startDate);
                newUtility.setEndDate(endDate);
                newUtility.setDaysInPeriod(Integer.parseInt(tempPeriod));
                Intent intent = getIntent();

                tempMode = intent.getIntExtra("mode", 0);
                //edit mode
                if(tempMode == 10){
                    User.getInstance().EditUtilityIntoUtilityList(position, newUtility);
                }
                else {
                    addNewUtility(newUtility);
                }

                finish();
            }
        });
    }

    private void showErrorToast(String message) {
        Toast.makeText(BillActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void addNewUtility(Utility utility) {
        Log.i(TAG, "Add button clicked");
        utility.setActive(true);
        utility = addUtilityToDatabase(utility);
        User.getInstance().addUtilityToUtilityList(utility);
    }

    private Utility addUtilityToDatabase(Utility utility) {
        UtilityDataSource db = new UtilityDataSource(this);
        db.open();
        Utility newUtility = db.insertUtility(utility);
        db.close();
        return newUtility;
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
            startDateSet = true;
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
                endDateText = (TextView) findViewById(R.id.endDateText);
                endDateText.setText(day_y + "/" + month_y  + "/" + year_y);
                endDateSet = true;
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
        currentAvgInput = (EditText) findViewById(R.id.currentAvgInput);
        previousAvgInput = (EditText) findViewById(R.id.previousAvgInput);

        startDateText = (TextView) findViewById(R.id.startDateText);
        endDateText = (TextView) findViewById(R.id.endDateText);

        long startTimeInMillis = utility.getStartDate().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis (startTimeInMillis);
        editStartYear = calendar.get(Calendar.YEAR);
        editStartMonth = calendar.get(Calendar.MONTH) + 1;
        editStartDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDateText.setText(editStartDay + "/" + editStartMonth  + "/" + editStartYear);

        long endTimeInMillis = utility.getStartDate().getTime();
        Calendar calendar_end = Calendar.getInstance();
        calendar.setTimeInMillis (endTimeInMillis);
        editEndYear = calendar_end.get(Calendar.YEAR);
        editEndMonth = calendar_end.get(Calendar.MONTH) + 1;
        editEndDay = calendar_end.get(Calendar.DAY_OF_MONTH);
        endDateText.setText(editEndDay + "/" + editEndMonth  + "/" + editEndYear);


        peopleInput.setText(String.valueOf(utility.getNumberOfPeople()));
        if(utility.getUtility_type().equals(Utility.GAS_NAME))
        {
            gasRb.setChecked(true);
            amountInput.setText(String.valueOf(utility.getNaturalGasUsed()));
            currentAvgInput.setText(String.valueOf(utility.getAverageGJCurrent()));
            previousAvgInput.setText(String.valueOf(utility.getAverageGJPrevious()));

        }
        if(utility.getUtility_type().equals(Utility.ELECTRICITY_NAME))
        {
            electricityRb.setChecked(true);
            amountInput.setText(String.valueOf(utility.getElectricUsed()));
            currentAvgInput.setText(String.valueOf(utility.getAverageKWhCurrent()));
            previousAvgInput.setText(String.valueOf(utility.getAverageKWhPrevious()));
        }
    }
}
