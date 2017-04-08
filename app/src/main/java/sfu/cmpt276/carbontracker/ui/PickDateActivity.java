package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.Database;
import sfu.cmpt276.carbontracker.ui.database.JourneyDataSource;
/*Activity to allow user to pick date for journeys*/
public class PickDateActivity extends AppCompatActivity {

    private Calendar calendar;

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
        date.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, monthOfYear);
                calendar.set(calendar.DAY_OF_MONTH, dayOfMonth);
            }
        });

        Button okButton = (Button) findViewById(R.id.calendarConfirm);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date newDate = calendar.getTime();
                if(newDate.getTime() > journeyDate.getTime()){
                    String msg = getString(R.string.pickEarlierDate);
                    Toast.makeText(PickDateActivity.this, msg, Toast.LENGTH_SHORT).show();
                }else {
                    setJourneyDate(newDate);
                    saveCurrentJourneyToDatabase();
                    Intent intent = new Intent(PickDateActivity.this, JourneyEmissionActivity.class);
                    startActivityForResult(intent, 0);
                }
            }
        });

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FullScreencall();
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

        private void setJourneyDate(Date date) {
            User.getInstance().getCurrentJourney().setDate(date);
        }

        private void saveCurrentJourneyToDatabase() {
            String TAG = "PickDateActivity";
            Log.i(TAG, "Saving Current Journey to Database");

            Journey journey = User.getInstance().getCurrentJourney();

            Database.getDB().addJourney(journey);
        }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == User.ACTIVITY_FINISHED_REQUESTCODE) {
                setResult(User.ACTIVITY_FINISHED_REQUESTCODE);
                finish();
            }
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
