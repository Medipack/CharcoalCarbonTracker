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

import sfu.cmpt276.carbontracker.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Thread for handling Notifications
 * Waits until specified time to show notification to user
 */
public class NotificationThread extends Thread {

    private static final int ADD_JOURNEY_NOTIFICATION_CODE = 1;
    private static final int ADD_BILL_NOTIFICATION_CODE = 2;
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

    private final NotificationHandler notificationHandler = new NotificationHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Log.i(NotificationThread.class.getName(), "Received notification code: " + msg.what);
            if(msg.what == NOTIFY)
                createNewNotification();

            else if(msg.what == QUIT_CODE) {
                Looper.myLooper().quitSafely();
            }
        }
    };

    NotificationThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Looper.prepare();
        Looper.loop();
    }

    private void createNewNotification() {
        // todo check journeys / bills entry dates
        // no journeys entered today? notify
        // only 1 journey entered today? notify
        // no bills in last 1.5 months? notify
        Intent intent = getNewJourneyIntent(context);

        PendingIntent pendingIntent = intentToPendingIntent(intent, context);

        Notification notification = buildNotification("Add a Journey for Today",
                "You haven't added any Journey's today. Tap to add!",
                NOTIFICATION_ICON,
                pendingIntent,
                context);

        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        manager.notify(ADD_JOURNEY_NOTIFICATION_CODE, notification);
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

    public Handler getNotificationhandler() {
        return notificationHandler;
    }
}


