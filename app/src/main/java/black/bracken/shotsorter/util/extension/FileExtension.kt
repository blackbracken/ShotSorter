package black.bracken.shotsorter.util.extension

import android.content.ContentUris
import android.content.Context
import android.provider.MediaStore
import java.io.File

fun File.deleteCertainly(context: Context) {
    val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val selection = MediaStore.Images.Media.DATA + " = ?"
    val selectionArguments = arrayOf(absolutePath)

    val resolver = context.contentResolver
    resolver.query(queryUri, projection, selection, selectionArguments, null)
            ?.takeIf { cursor -> cursor.moveToFirst() }
            ?.use { cursor ->
                val imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId)

                resolver.delete(imageUri, null, null)
            }
}