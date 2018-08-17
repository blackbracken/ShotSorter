package black.bracken.shotsorter;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import black.bracken.shotsorter.constant.PreferencesKeys;
import black.bracken.shotsorter.service.SortService;

/**
 * @author BlackBracken
 */
public final class AndroidBootReceiver extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (context.getSharedPreferences(PreferencesKeys.ROOT, Context.MODE_PRIVATE).getBoolean(PreferencesKeys.RUN_ON_STARTUP, false)) {
            SortService.start(context);
        }
    }

}
