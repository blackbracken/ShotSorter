package black.bracken.shotsorter.util

import android.content.Context
import android.net.Uri
import android.os.Handler
import black.bracken.shotsorter.util.extension.deleteCertainly
import java.io.File

/**
 * @author BlackBracken
 */
class ImageManipulator(
        private val imageUri: Uri,
        private val destination: File? = null,
        private val secondsDeletingLater: Int = -1
) {

    fun manipulate(context: Context) {
        var deleted = File(imageUri.path)

        destination?.also {
            val from = deleted
            val to = File("${it.absolutePath}/${imageUri.path.split("/").last()}")
            deleted = to

            from.copyTo(to)
            from.deleteCertainly(context)
        }

        if (secondsDeletingLater > 0) {
            Handler().postDelayed({
                deleted.deleteCertainly(context)
                if (deleted.exists()) deleted.delete()
            }, (secondsDeletingLater * 1000).toLong())
        }
    }

}
