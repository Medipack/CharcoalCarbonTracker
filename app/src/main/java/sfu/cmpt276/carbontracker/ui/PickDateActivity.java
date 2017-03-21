package sfu.cmpt276.carbontracker.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;

public class PickDateActivity extends AppCompatActivity {

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pick_date);
        calendar = Calendar.getInstance();
        DatePicker date = (DatePicker) findViewById(R.id.calendarDate);
        User user = User.getInstance();
        final Date journeyDate = user.getCurrentJourney().getDate();
        calendar.setTime(journeyDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date.updateDate(year, month, day);
        final DatePicker.OnDateChangedListener calendarDate = new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, monthOfYear);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
        Button okButton = (Button) findViewById(R.id.calendarConfirm);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date newDate = calendar.getTime();
                User.getInstance().getCurrentJourney().setDate(newDate);
                Intent intent = new Intent(PickDateActivity.this, JourneyEmissionActivity.class);
                startActivityForResult(intent, 0);
            }
        });}

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == User.ACTIITY_FINISHED_REQUESTCODE) {
                setResult(User.ACTIITY_FINISHED_REQUESTCODE);
                finish();
            }
        }
}
