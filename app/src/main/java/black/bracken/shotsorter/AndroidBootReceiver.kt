package black.bracken.shotsorter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

import black.bracken.shotsorter.constant.PreferencesKeys
import black.bracken.shotsorter.service.SortService

/**
 * @author BlackBracken
 */
class AndroidBootReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (context.getSharedPreferences(PreferencesKeys.ROOT, Context.MODE_PRIVATE).getBoolean(PreferencesKeys.RUN_ON_STARTUP, false)) {
            SortService.start(context)
        }
    }

}
