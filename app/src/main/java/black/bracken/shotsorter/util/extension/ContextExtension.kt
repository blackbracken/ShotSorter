package black.bracken.shotsorter.util.extension

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.support.v4.content.ContextCompat
import android.view.WindowManager

val Context.displayWidth
    get() = size.x

val Context.displayHeight
    get() = size.y

val Context.orientation
    get() = resources.configuration.orientation

private val Context.size
    get() = Point().also { display.getRealSize(it) }

private val Context.display
    get() = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

fun Context.hasPermission(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

