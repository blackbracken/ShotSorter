package black.bracken.shotsorter.data.repository

import android.net.Uri
import black.bracken.shotsorter.domain.repository.SortRepository
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import java.io.File

/**
 * @author BlackBracken
 */
class SingleSortRepository : SortRepository {

    override lateinit var imageUri: Uri

    override var destination: File? = null

    override var secondsToDeleteLater = 0

    override var shouldDeleteLater = false

}

val singleSortRepositoryModule = Kodein.Module("SingleSortRepositoryModule") {
    bind<SortRepository>() with provider { SingleSortRepository() }
}