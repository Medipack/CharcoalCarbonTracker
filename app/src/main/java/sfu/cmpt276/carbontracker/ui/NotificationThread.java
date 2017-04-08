package sfu.cmpt276.carbontracker.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import sfu.cmpt276.carbontracker.R;
import sfu.cmpt276.carbontracker.carbonmodel.Journey;
import sfu.cmpt276.carbontracker.carbonmodel.User;
import sfu.cmpt276.carbontracker.carbonmodel.Utility;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Thread for handling Notifications
 * Waits until specified time to show notification to user
 */
public final class NotificationThread extends TimerTask {

    // Send notification if outside threshold
    private static final long DAYS_ELAPSED_SINCE_LAST_BILL_NOTIFY_THRESHOLD = 45;

    //todo change notification icon to app icon (to be added)
    private static final int NOTIFICATION_ICON = R.mipmap.car1;

    // Thanks http://www.androiddesignpatterns.com/2013/01/inner-class-handler-memory-leak.html
    private static class NotificationHandler extends Handler {
        private final WeakReference<Thread> notificationThread;

        public NotificationHandler(Thread thread) {
            notificationThread = new WeakReference<Thread>(thread);
        }
    }

    private Context context;

    public static final int NOTIFY = 1;
    public static final int QUIT_CODE = 2;

    /*
    private final NotificationHandler notificationHandler = new NotificationHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Log.i(NotificationThread.class.getName(), "Received notification code: " + msg.what);
            if(msg.what == NOTIFY)
                createNewNotifications();

            else if(msg.what == QUIT_CODE) {
                Looper.myLooper().quitSafely();
            }
        }
    };
    */

    NotificationThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        createNewNotifications();
    }

    private void createNewNotifications() {
        int numJourneysAddedToday = getNumJourneysEnteredToday();
        displayAddNewJourneyNotification(numJourneysAddedToday);

        if(checkIfUtilityBillTimeElapsed())
            displayAddNewBillNotification();
    }

    private void displayAddNewBillNotification() {
        String title = context.getString(R.string.utility_remind_title);
        String content = context.getString(R.string.utility_remind);

        Intent intent = getNewBillIntent(context);

        PendingIntent pendingIntent = intentToPendingIntent(intent, context);

        Notification notification = buildNotification(title,
                content,
                NOTIFICATION_ICON,
                pendingIntent,
                context);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        manager.notify(NOTIFY, notification);
    }

    private boolean checkIfUtilityBillTimeElapsed() {
        Date today = new Date();

        if(User.getInstance().getUtilityList().getUtilities().size() <= 0)
            return true;

        for(Utility utility : User.getInstance().getUtilityList().getUtilities()) {
            Date utilityDateEntered = utility.getDateEntered();

            long daysElapsedInMillis = today.getTime() - utilityDateEntered.getTime();
            long daysElapsed = TimeUnit.DAYS.convert(daysElapsedInMillis, TimeUnit.MILLISECONDS);

            if(daysElapsed >= DAYS_ELAPSED_SINCE_LAST_BILL_NOTIFY_THRESHOLD)
                return true;
        }

        return false;
    }

    private int getNumJourneysEnteredToday() {
        Date today = new Date();
        Calendar calendar = GregorianCalendar.getInstance();

        calendar.setTime(today);
        int currentDay = calendar.get(Calendar.DAY_OF_YEAR);

        int numJourneysAddedToday = 0;
        for(Journey journey : User.getInstance().getJourneyList()) {
            Date journeyDateEntered = journey.getDateEntered();
            calendar.setTime(journeyDateEntered);
            int journeyDayEntered = calendar.get(Calendar.DAY_OF_YEAR);
            if(journeyDayEntered == currentDay){
                numJourneysAddedToday++;
            }
        }

        return numJourneysAddedToday;
    }

    private void displayAddNewJourneyNotification(int numJourneysAdded) {
        String title;
        String content;

        if(numJourneysAdded > 0) {
            title = context.getString(R.string.more_journeys);
            content = context.getString(R.string.you_entered) + numJourneysAdded
                    + context.getString(R.string._journey) + (numJourneysAdded == 1 ? "" : "s") // 1337 grammar fixer
                    + context.getString(R.string.want_to_add_more);
        } else {
            title = context.getString(R.string.add_a_journey_for_today);
            content = context.getString(R.string.journey_remind);
        }

        Intent intent = getNewJourneyIntent(context);

        PendingIntent pendingIntent = intentToPendingIntent(intent, context);

        Notification notification = buildNotification(title,
                content,
                NOTIFICATION_ICON,
                pendingIntent,
                context);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        manager.notify(NOTIFY, notification);
    }

    private Notification buildNotification(String title, String content, int iconId,
                                           PendingIntent pendingIntent, Context context) {
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(iconId)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent).build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        return notification;
    }

    private PendingIntent intentToPendingIntent(Intent intent, Context context) {
        return PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
    }

    private Intent getNewJourneyIntent(Context context) {
        return new Intent(context, TransportationModeActivity.class);
    }

    private Intent getNewBillIntent(Context context) {
        return new Intent(context, BillActivity.class);
    }

    /*
    public Handler getNotificationhandler() {
        return notificationHandler;
    }
    */
}


