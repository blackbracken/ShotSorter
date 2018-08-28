package black.bracken.shotsorter.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat

import black.bracken.shotsorter.R
import black.bracken.shotsorter.ScreenshotObserver
import black.bracken.shotsorter.activity.SortActivity
import black.bracken.shotsorter.util.AndroidUtil

/**
 * @author BlackBracken
 */
class SortService : Service() {

    private var screenshotObserver: ScreenshotObserver? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        isRunning = true

        this.screenshotObserver = ScreenshotObserver(this) { uri ->
            val sortIntent = Intent(this, SortActivity::class.java)
            sortIntent.putExtra(URI_KEY, uri)

            startActivity(sortIntent)
        }
        this.screenshotObserver!!.startWatching()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false

        this.screenshotObserver!!.stopWatching()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val builder: NotificationCompat.Builder
        if (AndroidUtil.higherThan(Build.VERSION_CODES.O)) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager.getNotificationChannel(NOTIFICATION_ID) == null) {
                notificationManager.createNotificationChannel(
                        NotificationChannel(NOTIFICATION_ID, NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_LOW)
                )
            }
            builder = NotificationCompat.Builder(this, NOTIFICATION_ID)
        } else {
            builder = NotificationCompat.Builder(this)
        }

        builder.setContentTitle(NOTIFICATION_TITLE)
        builder.setSmallIcon(R.drawable.ic_launcher_background)

        startForeground(1, builder.build())

        return Service.START_NOT_STICKY
    }

    companion object {

        val URI_KEY = "URI"

        private val NOTIFICATION_TITLE = "SortService is running"
        private val NOTIFICATION_ID = "foreground"

        // LINK: https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
        var isRunning = false
            private set

        fun start(context: Context) {
            val intent = Intent(context, SortService::class.java)

            if (AndroidUtil.higherThan(Build.VERSION_CODES.O)) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

}