package black.bracken.shotsorter.domain.repository

/**
 * @author BlackBracken
 */
interface SettingRepository {

    var observedScreenshotDirectoryPath: String

    var shouldRunOnStartup: Boolean

}