package black.bracken.shotsorter.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.WindowManager;

import java.util.Objects;

/**
 * @author BlackBracken
 */
public final class ContextUtil {

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static int getOrientation(Context context) {
        return context.getResources().getConfiguration().orientation;
    }

    public static int getHardwareWidth(Context context) {
        return getRealSize(context).x;
    }

    public static int getHardwareHeight(Context context) {
        return getRealSize(context).y;
    }

    private static Point getRealSize(Context context) {
        Point point = new Point();
        getDisplay(context).getRealSize(point);

        return point;
    }

    private static Display getDisplay(Context context) {
        return ((WindowManager) Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
    }

}
