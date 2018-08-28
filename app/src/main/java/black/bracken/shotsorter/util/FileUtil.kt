package black.bracken.shotsorter.util

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import java.io.File

/**
 * @author BlackBracken
 */
object FileUtil {

    // LINK: https://stackoverflow.com/questions/10716642/android-deleting-an-image
    fun deleteImageCertainly(context: Context, file: File) {
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = MediaStore.Images.Media.DATA + " = ?"
        val selectionArguments = arrayOf(file.absolutePath)

        val resolver = context.contentResolver
        resolver.query(queryUri, projection, selection, selectionArguments, null)!!.use { cursor ->
            assert(cursor != null)

            if (cursor.moveToFirst()) {
                val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)
                resolver.delete(imageUri, null, null)
            }
        }
    }

}
