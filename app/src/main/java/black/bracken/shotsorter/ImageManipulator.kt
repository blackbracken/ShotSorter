package black.bracken.shotsorter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import black.bracken.shotsorter.util.FileUtil
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author BlackBracken
 */
class ImageManipulator(private var image: File?) {
    private var destination: File? = null
    private var secondsDeletingLater: Int = 0

    @SuppressLint("NewApi")
    fun manipulate(context: Context) {
        if (image == null) {
            throw IllegalArgumentException("Image must not be null")
        }

        if (destination != null) {
            try {
                Files.copy(Paths.get(image!!.toURI()), Paths.get(destination!!.toURI()))
                FileUtil.deleteImageCertainly(context, image!!)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }

        }

        if (secondsDeletingLater > 0) {
            val deleted = if (destination != null) destination else image

            Handler().postDelayed({
                FileUtil.deleteImageCertainly(context, deleted)
                if (deleted.exists()) {
                    deleted.delete()
                }
            }, (secondsDeletingLater * 1000).toLong())
        }
    }

    fun setImage(image: File) {
        this.image = image
    }

    fun setDestination(destination: File) {
        this.destination = destination
    }

    fun setDestinationAsFolder(folder: File) {
        setDestination(File(folder.path + File.separator + image!!.name))
    }

    fun setSecondsDeletingLater(secondsDeletingLater: Int) {
        this.secondsDeletingLater = if (0 < secondsDeletingLater) secondsDeletingLater else 0
    }

}
