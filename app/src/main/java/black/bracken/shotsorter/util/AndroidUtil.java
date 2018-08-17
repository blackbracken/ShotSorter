package black.bracken.shotsorter.util;

import android.os.Build;

/**
 * @author BlackBracken
 */
public final class AndroidUtil {

    private AndroidUtil() {
    }

    public static boolean higherThan(int version) {
        return version <= Build.VERSION.SDK_INT;
    }

}