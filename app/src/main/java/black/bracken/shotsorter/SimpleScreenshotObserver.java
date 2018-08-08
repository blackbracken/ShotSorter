package black.bracken.shotsorter;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.Consumer;

import black.bracken.shotsorter.util.AndroidUtil;

/**
 * スクリーンショットを撮ることに反応する単純なオブザーバ.
 *
 * @author BlackBracken
 */
public final class SimpleScreenshotObserver extends FileObserver {

    private static final String IMAGE_EXT = ".png";
    private static final String SCREENSHOT_DIR_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
            + File.separator + "Screenshots" + File.separator;

    private final Consumer<Uri> action;

    public SimpleScreenshotObserver(final Consumer<Uri> action) {
        super(SCREENSHOT_DIR_PATH, FileObserver.CLOSE_WRITE);

        this.action = action;
    }

    public final void onEvent(int event, @Nullable String fileName) {
        if (fileName == null || !fileName.endsWith(IMAGE_EXT)) return;

        File file = new File(SCREENSHOT_DIR_PATH + fileName);
        try (FileInputStream stream = new FileInputStream(file)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, options);

            if (options.outWidth == AndroidUtil.getHardwareWidth() && options.outHeight == AndroidUtil.getHardwareHeight()) {
                action.accept(Uri.fromFile(file));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
