package sfu.cmpt276.carbontracker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

import sfu.cmpt276.carbontracker.carbonmodel.User;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Thread for handling Notifications
 * Waits until specified time to show notification to user
 */
public class NotificationThread extends Thread {

    // Thanks http://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html
    private static class NotificationHandler extends Handler {
        private final WeakReference<Thread> notificationThread;

        public NotificationHandler(Thread thread) {
            notificationThread = new WeakReference<Thread>(thread);
        }
    }

    private NotificationHandler notificationHandler = new NotificationHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Log.i(NotificationThread.class.getName(), "Received notification code: " + msg.what);
            if(msg.what == ADD_JOURNEY_CODE)
                displayAddJourneyNotification();

            else if(msg.what == ADD_BILL_CODE)
                displayAddBillNotification();

            else if(msg.what == QUIT_CODE) {
                Looper.myLooper().quitSafely();
            }
        }
    };

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

    public Handler getNotificationhandler() {
        return notificationHandler;
    }
}


