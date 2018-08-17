package black.bracken.shotsorter.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.SimpleScreenshotObserver;
import black.bracken.shotsorter.activity.SortActivity;
import black.bracken.shotsorter.util.AndroidUtil;

/**
 * @author BlackBracken
 */
public final class SortService extends Service {

    public static final String URI_KEY = "URI";

    private static final String MESSAGE_INITIALIZE = "SortService has been initialized";
    private static final String NOTIFICATION_TITLE = "SortService is running";
    private static final String NOTIFICATION_ID = "foreground";

    // LINK: https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
    private static boolean isRunning = false;

    private SimpleScreenshotObserver screenshotObserver;

    public static void start(Context context) {
        Intent intent = new Intent(context, SortService.class);

        if (AndroidUtil.higherThan(Build.VERSION_CODES.O)) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    public static boolean isRunning() {
        return isRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // not implemented
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRunning = true;

        this.screenshotObserver = new SimpleScreenshotObserver(this, uri -> {
            Intent sortIntent = new Intent(this, SortActivity.class);
            sortIntent.putExtra(URI_KEY, uri);

            startActivity(sortIntent);
        });
        this.screenshotObserver.startWatching();

        Toast.makeText(this, MESSAGE_INITIALIZE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;

        this.screenshotObserver.stopWatching();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder;
        if (AndroidUtil.higherThan(Build.VERSION_CODES.O)) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;

            if (notificationManager.getNotificationChannel(NOTIFICATION_ID) == null) {
                notificationManager.createNotificationChannel(
                        new NotificationChannel(NOTIFICATION_ID, NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_LOW)
                );
            }
            builder = new NotificationCompat.Builder(this, NOTIFICATION_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setContentTitle(NOTIFICATION_TITLE);
        builder.setSmallIcon(R.drawable.ic_launcher_background);

        startForeground(1, builder.build());

        return START_NOT_STICKY;
    }

}