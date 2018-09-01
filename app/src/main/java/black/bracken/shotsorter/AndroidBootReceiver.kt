package black.bracken.shotsorter

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import black.bracken.shotsorter.data.repository.singleSettingRepositoryModule
import black.bracken.shotsorter.domain.repository.SettingRepository
import black.bracken.shotsorter.service.SortService
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

/**
 * @author BlackBracken
 */
class AndroidBootReceiver : BroadcastReceiver(), KodeinAware {

    override val kodein = Kodein {
        import(singleSettingRepositoryModule)
    }

    private val settingRepository: SettingRepository by instance()

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        if (settingRepository.shouldRunOnStartup) {
            SortService.start(context)
        }
    }

}
