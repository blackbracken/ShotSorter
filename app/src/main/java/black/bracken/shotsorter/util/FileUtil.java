package black.bracken.shotsorter.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * @author BlackBracken
 */
public final class FileUtil {

    private FileUtil() {
    }

    // LINK: https://stackoverflow.com/questions/10716642/android-deleting-an-image
    public static void deleteImageCertainly(Context context, File file) {
        Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media._ID};
        String selection = MediaStore.Images.Media.DATA + " = ?";
        String[] selectionArguments = {file.getAbsolutePath()};

        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(queryUri, projection, selection, selectionArguments, null)) {
            assert cursor != null;

            if (cursor.moveToFirst()) {
                long imageId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageId);
                resolver.delete(imageUri, null, null);
            }
        }
    }

}
