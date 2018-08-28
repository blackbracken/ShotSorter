package black.bracken.shotsorter.util

import android.os.Build

/**
 * @author BlackBracken
 */
object AndroidUtil {

    fun higherThan(version: Int): Boolean {
        return version <= Build.VERSION.SDK_INT
    }

}