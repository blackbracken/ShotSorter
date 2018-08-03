package black.bracken.shotsorter;

import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.FileObserver;
import android.os.SystemClock;
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
    private static final String SCREENSHOT_DIR_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath().substring(1)
            + File.separator + "Screenshots" + File.separator;
    private static final int SAVING_COOL_TIME = 1000; // ms

    private final Consumer<String> action;

    public SimpleScreenshotObserver(final Consumer<String> action) {
        super(SCREENSHOT_DIR_PATH, FileObserver.CREATE);

        this.action = action;
    }

    @Override
    public final void onEvent(int event, @Nullable String fileName) {
        if (fileName == null || !fileName.endsWith(IMAGE_EXT)) return;

        SystemClock.sleep(SAVING_COOL_TIME);

        // HACK: make Bitmap without InputStream for brevity
        try (FileInputStream stream = new FileInputStream(new File(SCREENSHOT_DIR_PATH + fileName))) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(stream, null, options);

            if (options.outWidth == AndroidUtil.getHardwareWidth() && options.outHeight == AndroidUtil.getHardwareHeight()) {
                action.accept(fileName);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
