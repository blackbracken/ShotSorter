package black.bracken.shotsorter.view

import android.net.Uri

/**
 * @author BlackBracken
 */
interface SortView {

    fun close()

    fun layWallpaper(uri: Uri)

    fun toggleWhetherCanInputDelaySeconds(shouldDeleteLater: Boolean)

    fun openDestinationSelector()

}