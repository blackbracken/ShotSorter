package black.bracken.shotsorter.listener;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import black.bracken.shotsorter.ShotSorter;

/**
 * Androidシステムの起動時に呼び出されるリスナ.
 *
 * @author BlackBracken
 */
public final class AndroidBootListener extends BroadcastReceiver {

    private static final String STARTUP_TOAST_MESSAGE = "ShotSorter initialized";

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(ShotSorter.getInstance(), STARTUP_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
    }

}
