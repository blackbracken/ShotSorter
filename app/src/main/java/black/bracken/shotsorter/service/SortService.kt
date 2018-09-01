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
import black.bracken.shotsorter.data.repository.singleSettingRepositoryModule
import black.bracken.shotsorter.domain.repository.SettingRepository
import black.bracken.shotsorter.view.activity.SortActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * @author BlackBracken
 */
class SortService : Service(), KodeinAware {

    companion object {
        const val CODE_IMAGE_URI = "URI"

        private const val NOTIFICATION_TITLE = "SortService is running"
        private const val NOTIFICATION_ID = "foreground"

        // LINK: https://stackoverflow.com/questions/600207/how-to-check-if-a-service-is-running-on-android
        var isRunning = false
            private set

        fun start(context: Context) {
            val intent = Intent(context, SortService::class.java)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun stop(context: Context) {
            val intent = Intent(context, SortService::class.java)

            context.stopService(intent)
        }
    }

    override val kodein = Kodein {
        import(singleSettingRepositoryModule)
    }

    private lateinit var screenshotObserver: ScreenshotObserver
    private val settingRepository: SettingRepository by instance()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        isRunning = true

        this.screenshotObserver = ScreenshotObserver(this, settingRepository.observedScreenshotDirectoryPath) { uri ->
            val sortIntent = Intent(this, SortActivity::class.java)
            sortIntent.putExtra(CODE_IMAGE_URI, uri)

            startActivity(sortIntent)
        }
        this.screenshotObserver.startWatching()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false

        this.screenshotObserver.stopWatching()
    }

    @Suppress("DEPRECATION")
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val builder = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this)
        } else {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (notificationManager.getNotificationChannel(NOTIFICATION_ID) == null) {
                notificationManager.createNotificationChannel(NotificationChannel(NOTIFICATION_ID, NOTIFICATION_TITLE, NotificationManager.IMPORTANCE_LOW))
            }

            NotificationCompat.Builder(this, NOTIFICATION_ID)
        }

        builder.setContentTitle(NOTIFICATION_TITLE)
        builder.setSmallIcon(R.drawable.ic_launcher_background)

        startForeground(1, builder.build())

        return START_NOT_STICKY
    }

}