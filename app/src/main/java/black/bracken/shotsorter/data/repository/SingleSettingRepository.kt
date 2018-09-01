package black.bracken.shotsorter.data.repository

import android.content.Context
import android.os.Environment
import black.bracken.shotsorter.ShotSorter
import black.bracken.shotsorter.domain.repository.SettingRepository
import black.bracken.shotsorter.util.extension.write
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

/**
 * @author BlackBracken
 */
object SingleSettingRepository : SettingRepository {

    const val PREFERENCES_ROOT = "ShotSorter"
    const val PREFERENCES_SCREENSHOT_DIR_PATH = "ObservedScreenshotDirectoryPath"
    const val PREFERENCES_RUN_ON_STARTUP = "RunOnStartup"

    private val context by lazy { ShotSorter.appContext }
    private val defaultScreenshotDirectory = "${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath}/Screenshots/"

    private val preferences by lazy { context.getSharedPreferences(PREFERENCES_ROOT, Context.MODE_PRIVATE) }

    override var observedScreenshotDirectoryPath: String
        get() = preferences.getString(PREFERENCES_SCREENSHOT_DIR_PATH, defaultScreenshotDirectory)
        set(path) {
            preferences.write { putString(PREFERENCES_SCREENSHOT_DIR_PATH, path) }
        }

    override var shouldRunOnStartup: Boolean
        get() = preferences.getBoolean(PREFERENCES_RUN_ON_STARTUP, false)
        set(shouldRunOnStartup) {
            preferences.write { putBoolean(PREFERENCES_RUN_ON_STARTUP, shouldRunOnStartup) }
        }

}

val singleSettingRepositoryModule = Kodein.Module("SingleSettingRepositoryModule") {
    bind<SettingRepository>() with singleton { SingleSettingRepository }
}