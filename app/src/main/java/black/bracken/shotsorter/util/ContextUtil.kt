package black.bracken.shotsorter.util

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.view.Display
import android.view.WindowManager
import java.util.*

/**
 * @author BlackBracken
 */
object ContextUtil {

    fun hasPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun getOrientation(context: Context): Int {
        return context.resources.configuration.orientation
    }

    fun getHardwareWidth(context: Context): Int {
        return getRealSize(context).x
    }

    fun getHardwareHeight(context: Context): Int {
        return getRealSize(context).y
    }

    private fun getRealSize(context: Context): Point {
        val point = Point()
        getDisplay(context).getRealSize(point)

        return point
    }

    private fun getDisplay(context: Context): Display {
        return (Objects.requireNonNull(context.getSystemService(Context.WINDOW_SERVICE)) as WindowManager).defaultDisplay
    }

}
