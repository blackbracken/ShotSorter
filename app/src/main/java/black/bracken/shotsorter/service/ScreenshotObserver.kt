package black.bracken.shotsorter.service

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.FileObserver
import black.bracken.shotsorter.util.extension.displayHeight
import black.bracken.shotsorter.util.extension.displayWidth
import java.io.File
import java.io.FileInputStream

/**
 * @author BlackBracken
 */
class ScreenshotObserver(context: Context, private val screenshotDirectoryPath: String, private val action: (Uri) -> Unit) : FileObserver(screenshotDirectoryPath, FileObserver.CLOSE_WRITE) {

    companion object {
        private const val IMAGE_EXT = ".png"
    }

    private val displayWidth = context.displayWidth
    private val displayHeight = context.displayHeight

    override fun onEvent(event: Int, fileName: String?) {
        if (fileName == null || !fileName.endsWith(IMAGE_EXT)) return

        val file = File("$screenshotDirectoryPath$fileName")
        FileInputStream(file).use { stream ->
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(stream, null, options)

            if (setOf(options.outWidth, options.outHeight) == setOf(displayWidth, displayHeight)) {
                action(Uri.fromFile(file))
            }
        }
    }

}