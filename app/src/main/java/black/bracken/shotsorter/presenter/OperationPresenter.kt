package black.bracken.shotsorter.presenter

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast
import black.bracken.shotsorter.R
import black.bracken.shotsorter.ShotSorter
import black.bracken.shotsorter.data.repository.singleSettingRepositoryModule
import black.bracken.shotsorter.domain.repository.SettingRepository
import black.bracken.shotsorter.service.SortService
import black.bracken.shotsorter.util.extension.hasPermission
import black.bracken.shotsorter.view.OperationView
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.File

/**
 * @author BlackBracken
 */
class OperationPresenter(private val view: OperationView) : Presenter, KodeinAware {

    override val kodein = Kodein {
        import(singleSettingRepositoryModule)
    }

    private val settingRepository: SettingRepository by instance()

    init {
        view.toggleStartupIsChecked(settingRepository.shouldRunOnStartup)
    }

    fun onClickToChangeObservedDirectory() {
        if (SortService.isRunning) {
            Toast.makeText(ShotSorter.appContext, R.string.toast_cannot_change_observed, Toast.LENGTH_LONG).show()
        } else {
            view.openObservedDirectorySelector()
        }
    }

    fun onToggleActivate(isTurnedOn: Boolean, context: Context) {
        view.toggleActivateIsChecked(isTurnedOn)

        if (isTurnedOn) {
            SortService.start(context)
        } else {
            SortService.stop(context)
        }
    }

    fun onToggleRunOnStartup(isTurnedOn: Boolean) {
        settingRepository.shouldRunOnStartup = isTurnedOn
        view.toggleStartupIsChecked(isTurnedOn)
    }

    fun onChangeObservedDirectory(directory: File) {
        settingRepository.observedScreenshotDirectoryPath = "${directory.absolutePath}/"
    }

    fun requestPermissionsIfNeeded(activity: Activity) {
        if (!activity.hasPermission(Manifest.permission_group.STORAGE)) {
            view.requestPermission()
        }
    }

}

val operationPresenterModule = Kodein.Module("OperationPresenter") {
    bind<OperationPresenter>() with singleton { OperationPresenter(instance()) }
}