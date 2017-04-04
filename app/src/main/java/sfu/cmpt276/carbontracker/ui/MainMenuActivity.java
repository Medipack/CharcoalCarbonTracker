package sfu.cmpt276.carbontracker.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.ui.database.Database;

/*  Menu Activity displays main menu
* */
public class MainMenuActivity extends AppCompatActivity {

    private NotificationThread notificationThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Database.getDB().initializeDatabase(this);

        setupMainDirectory();
        setupNewJourneyBtn();
        setupCarbonTotalsBtn();

        setupCarbon();
        setupUtility();
        setupGraph();

        setupNotificationThread();
    }

    private void setupNotificationThread() {
        notificationThread = new NotificationThread(this);
        notificationThread.start();

        int notificationHour = 19;

        Date currentDate = new Date();

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(currentDate);

        int currentHoursIn24HourFormat = calendar.get(Calendar.HOUR_OF_DAY);
        if(currentHoursIn24HourFormat > notificationHour)
            calendar.add(Calendar.DAY_OF_YEAR, 1);

        calendar.set(Calendar.HOUR_OF_DAY, notificationHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);


        //todo this is debug only
        calendar.setTime(currentDate);
        calendar.add(Calendar.SECOND, 10);

        long nextNotificationDateInMillis;
        nextNotificationDateInMillis = calendar.getTimeInMillis();

        Handler handler = null;
        while(handler == null) {
            handler = notificationThread.getNotificationhandler();
        }

        long timeDifference = nextNotificationDateInMillis - (new Date()).getTime();
        long systemTime = SystemClock.uptimeMillis();

        long nextNotificationDateInSystemUptime = systemTime + timeDifference;

        handler.sendMessageAtTime(handler.obtainMessage(NotificationThread.NOTIFY, 0, 0, null),
                nextNotificationDateInSystemUptime);

        Log.i(MainMenuActivity.class.getName(), "Set notification for " + calendar.getTime());
    }


    private void setupCarbon() {
        TextView carbon = (TextView) findViewById(R.id.carbon);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/Super.ttf");
        carbon.setTypeface(face);

    }

    private void setupMainDirectory(){
        if(User.getInstance().directoryNotSetup()){
            InputStream input = getResources().openRawResource(R.raw.vehicles);
            User.getInstance().setUpDirectory(input);
        }
    }

    private void startNewJourney() {
        User.getInstance().createNewCurrentJourney();
        Intent intent = new Intent(MainMenuActivity.this, TransportationModeActivity.class);
        startActivity(intent);
    }

    private void setupNewJourneyBtn()
    {
        Button newJourneyBtn = (Button)findViewById(R.id.createJourneyBtn);
        newJourneyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewJourney();
            }
        });
    }

    private void setupCarbonTotalsBtn()
    {
        Button carbonTotalsBtn = (Button)findViewById(R.id.viewCarbonTotalsBtn);
        carbonTotalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, JourneyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUtility() {
        Button utilityBtn = (Button)findViewById(R.id.createUtilityBtn);
        utilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, UtilityActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupGraph() {
        Button showGraphBtn = (Button) findViewById(R.id.showGraphBtn);
        showGraphBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });

    }

}