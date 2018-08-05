package black.bracken.shotsorter.listener;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import black.bracken.shotsorter.ShotSorter;

/**
 * Androidシステムの起動時に呼び出されるリスナ.
 *
 * @author BlackBracken
 */
public final class AndroidBootListener extends BroadcastReceiver {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        ShotSorter.startServiceIfNot(context);
    }

}
