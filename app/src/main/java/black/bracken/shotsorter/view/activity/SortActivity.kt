package black.bracken.shotsorter.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.method.KeyListener
import black.bracken.shotsorter.R
import black.bracken.shotsorter.data.repository.singleSortRepositoryModule
import black.bracken.shotsorter.presenter.SortPresenter
import black.bracken.shotsorter.presenter.sortPresenterModule
import black.bracken.shotsorter.service.SortService
import black.bracken.shotsorter.util.SimpleTextWatcher
import black.bracken.shotsorter.view.SortView
import kotlinx.android.synthetic.main.activity_sort.*
import lib.folderpicker.FolderPicker
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import java.io.File

/**
 * @author BlackBracken
 */
class SortActivity : Activity(), SortView, KodeinAware {

    companion object {
        private const val CODE_FOLDER_SELECT = 8512
    }

    override val kodein = Kodein {
        bind<SortView>() with instance(this@SortActivity)
        import(sortPresenterModule)
        import(singleSortRepositoryModule)
    }

    private val presenter: SortPresenter by instance()
    private lateinit var defaultKeyListener: KeyListener

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_sort)
        defaultKeyListener = editDelaySeconds.keyListener

        presenter.onReceiveScreenshotUri(intent.getParcelableExtra(SortService.CODE_IMAGE_URI))

        editDelaySeconds.addTextChangedListener(SimpleTextWatcher { charSequence, _, _, _ -> presenter.onEditDelaySeconds(charSequence) })
        switchDeleteLater.setOnCheckedChangeListener { _, isTurnedOn -> presenter.onSwitchWhetherDeleteLater(isTurnedOn) }
        buttonChangeDestination.setOnClickListener { _ -> openDestinationSelector() }
        buttonExit.setOnClickListener { _ -> presenter.onExit(true, this) }
        buttonExitWithoutApplying.setOnClickListener { _ -> presenter.onExit(false, this) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            CODE_FOLDER_SELECT -> presenter.onChangeDestination(File(intent.extras.getString("data")))
        }
    }

    override fun close() = finish()

    override fun layWallpaper(uri: Uri) {
        layoutSort.background = Drawable.createFromStream(contentResolver.openInputStream(uri), uri.toString())
    }

    override fun toggleWhetherCanInputDelaySeconds(shouldDeleteLater: Boolean) {
        editDelaySeconds.keyListener = if (shouldDeleteLater) defaultKeyListener else null
    }

    override fun openDestinationSelector() {
        startActivityForResult(Intent(this, FolderPicker::class.java), CODE_FOLDER_SELECT)
    }

}
