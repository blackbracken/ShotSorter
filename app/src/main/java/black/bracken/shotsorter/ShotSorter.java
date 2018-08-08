package black.bracken.shotsorter;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import black.bracken.shotsorter.activity.SortActivity;
import black.bracken.shotsorter.util.AndroidUtil;

/**
 * A stationed service of ShotSorter.
 *
 * @author BlackBracken
 */
public final class ShotSorter extends Service {

    public static final String URI_KEY = "URI";

    private static final String INITIALIZE_MESSAGE = "ShotSorter has been initialized";
    private static final String NOTIFICATION_TITLE = "ShotSorter is running";
    private static final String NOTIFICATION_ID = "foreground";

    private static ShotSorter instance = null;

    private SimpleScreenshotObserver screenshotObserver;

    public static ShotSorter getInstance() {
        return instance;
    }

    public static void startServiceIfNot(Context context) {
        if (instance == null) {
            Intent intent = new Intent(context, ShotSorter.class);

            if (AndroidUtil.higherThan(Build.VERSION_CODES.O)) {
                context.startForegroundService(intent);
            } else {
                context.startService(intent);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null; // not implemented
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.screenshotObserver = new SimpleScreenshotObserver(uri -> {
            Intent sortIntent = new Intent(this, SortActivity.class);
            sortIntent.putExtra(URI_KEY, uri);

            startActivity(sortIntent);
        });
        this.screenshotObserver.startWatching();

        instance = this;

        Toast.makeText(this, INITIALIZE_MESSAGE, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        instance = null;
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

        return START_STICKY;
    }

}