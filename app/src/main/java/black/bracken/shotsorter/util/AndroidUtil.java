package black.bracken.shotsorter.util;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

import java.util.Objects;

import black.bracken.shotsorter.ShotSorter;

/**
 * @author BlackBracken
 */
public final class AndroidUtil {

    private AndroidUtil() {
    }

    public static boolean higherThan(int version) {
        return version <= Build.VERSION.SDK_INT;
    }

    public static int getHardwareWidth() {
        return getRealSize().x;
    }

    public static int getHardwareHeight() {
        return getRealSize().y;
    }

    private static Point getRealSize() {
        Point point = new Point();
        getDisplay().getRealSize(point);

        return point;
    }

    private static Display getDisplay() {
        return ((WindowManager) Objects.requireNonNull(ShotSorter.getInstance().getSystemService(Context.WINDOW_SERVICE))).getDefaultDisplay();
    }

}