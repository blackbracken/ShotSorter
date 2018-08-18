package black.bracken.shotsorter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import black.bracken.shotsorter.util.ContextUtil;

/**
 * @author BlackBracken
 */
public final class ScreenshotObserver extends FileObserver {

    public static final String SCREENSHOT_DIR_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
            + File.separator + "Screenshots" + File.separator;

    private static final String IMAGE_EXT = ".png";

    private final Consumer<Uri> action;
    private final int displayWidth, displayHeight;

    public ScreenshotObserver(Context context, Consumer<Uri> action) {
        super(SCREENSHOT_DIR_PATH, FileObserver.CLOSE_WRITE);

        this.action = action;
        this.displayWidth = ContextUtil.getHardwareWidth(context);
        this.displayHeight = ContextUtil.getHardwareHeight(context);
    }

    public final void onEvent(int event, @Nullable String fileName) {
        if (fileName == null || !fileName.endsWith(IMAGE_EXT)) return;

        File file = new File(SCREENSHOT_DIR_PATH + fileName);
        try (FileInputStream stream = new FileInputStream(file)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, options);

            if (options.outWidth == displayWidth && options.outHeight == displayHeight
                    || options.outWidth == displayHeight && options.outHeight == displayWidth) {
                action.accept(Uri.fromFile(file));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
