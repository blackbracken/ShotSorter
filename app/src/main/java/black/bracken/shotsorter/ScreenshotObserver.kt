package black.bracken.shotsorter

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.os.FileObserver
import black.bracken.shotsorter.util.ContextUtil
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.function.Consumer

/**
 * @author BlackBracken
 */
class ScreenshotObserver(context: Context, private val action: Consumer<Uri>) : FileObserver(SCREENSHOT_DIR_PATH, FileObserver.CLOSE_WRITE) {
    private val displayWidth: Int
    private val displayHeight: Int

    init {
        this.displayWidth = ContextUtil.getHardwareWidth(context)
        this.displayHeight = ContextUtil.getHardwareHeight(context)
    }

    override fun onEvent(event: Int, fileName: String?) {
        if (fileName == null || !fileName.endsWith(IMAGE_EXT)) return

        val file = File(SCREENSHOT_DIR_PATH + fileName)
        try {
            FileInputStream(file).use { stream ->
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                BitmapFactory.decodeStream(stream, null, options)

                if (options.outWidth == displayWidth && options.outHeight == displayHeight || options.outWidth == displayHeight && options.outHeight == displayWidth) {
                    action.accept(Uri.fromFile(file))
                }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

    }

    companion object {

        val SCREENSHOT_DIR_PATH = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath
                + File.separator + "Screenshots" + File.separator)

        private val IMAGE_EXT = ".png"
    }

}
