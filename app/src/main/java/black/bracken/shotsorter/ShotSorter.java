package black.bracken.shotsorter;

import android.app.Application;
import android.util.Log;

/**
 * An application named ShotSorter.
 *
 * @author BlackBracken
 */
public final class ShotSorter extends Application {

    private static ShotSorter instance;

    private SimpleScreenshotObserver screenshotObserver;

    public static ShotSorter getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // TODO: remove debug message
        this.screenshotObserver = new SimpleScreenshotObserver(path -> Log.d("ShotSorter", "PATH: " + path));
        this.screenshotObserver.startWatching();

        instance = this;
    }

}