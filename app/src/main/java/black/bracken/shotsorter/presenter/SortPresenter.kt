package black.bracken.shotsorter.presenter

import android.content.Context
import android.net.Uri
import black.bracken.shotsorter.data.repository.singleSortRepositoryModule
import black.bracken.shotsorter.domain.repository.SortRepository
import black.bracken.shotsorter.util.ImageManipulator
import black.bracken.shotsorter.view.SortView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.io.File

/**
 * @author BlackBracken
 */
class SortPresenter(private val view: SortView) : Presenter, KodeinAware {

    override val kodein = Kodein {
        import(singleSortRepositoryModule)
    }

    private val sortRepository: SortRepository by instance()

    fun onChangeDestination(destination: File) {
        sortRepository.destination = destination
    }

    fun onEditDelaySeconds(input: CharSequence) {
        sortRepository.secondsToDeleteLater = input.toString().toIntOrNull() ?: return
    }

    fun onExit(withApplying: Boolean, context: Context) {
        if (withApplying) {
            ImageManipulator(
                    sortRepository.imageUri,
                    sortRepository.destination,
                    if (sortRepository.shouldDeleteLater) sortRepository.secondsToDeleteLater else -1
            ).manipulate(context)
        }

        view.close()
    }

    fun onReceiveScreenshotUri(uri: Uri) {
        sortRepository.imageUri = uri
        view.layWallpaper(uri)
    }

    fun onSwitchWhetherDeleteLater(shouldDeleteLater: Boolean) {
        sortRepository.shouldDeleteLater = shouldDeleteLater
        view.toggleWhetherCanInputDelaySeconds(shouldDeleteLater)
    }

}

val sortPresenterModule = Kodein.Module("SortPresenter") {
    bind<SortPresenter>() with provider { SortPresenter(instance()) }
}