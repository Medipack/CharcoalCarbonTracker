package sfu.cmpt276.carbontracker.ui;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import sfu.cmpt276.carbontracker.carbonmodel.User;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Thread for handling Notifications
 * Waits until specified time to show notification to user
 */
public class NotificationThread extends Thread {
    private Handler notificationhandler;

    public static final int ADD_JOURNEY_CODE = 1;
    public static final int ADD_BILL_CODE = 2;
    public static final int QUIT_CODE = 3;

    private Context context;

    NotificationThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Looper.prepare();

        notificationhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == ADD_JOURNEY_CODE)
                    displayAddJourneyNotification();

                else if(msg.what == ADD_BILL_CODE)
                    displayAddBillNotification();

                else if(msg.what == QUIT_CODE) {
                    Looper.myLooper().quitSafely();
                }
            }
        };

        Looper.loop();
    }

    private void displayAddJourneyNotification() {
        // todo notification
        startNewJourney();
    }

    private void displayAddBillNotification() {
        // todo
    }

    private void startNewJourney() {
        User.getInstance().createNewCurrentJourney();
        Intent intent = new Intent(context, TransportationModeActivity.class);
        Bundle emptyBundle = new Bundle();
        startActivity(context, intent, emptyBundle);
    }
}


