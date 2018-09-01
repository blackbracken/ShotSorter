package black.bracken.shotsorter.domain.repository

import android.net.Uri
import java.io.File

/**
 * @author BlackBracken
 */
interface SortRepository {

    var imageUri: Uri

    var destination: File?

    var shouldDeleteLater: Boolean

    var secondsToDeleteLater: Int

}