package black.bracken.shotsorter.view.activity

import android.Manifest
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import black.bracken.shotsorter.R
import black.bracken.shotsorter.presenter.OperationPresenter
import black.bracken.shotsorter.presenter.operationPresenterModule
import black.bracken.shotsorter.view.OperationView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.kcontext


/**
 * @author BlackBracken
 */
class OperationActivity : AppCompatActivity(), OperationView, KodeinAware {

    override val kodeinContext = kcontext(this)

    override val kodein = Kodein {
        bind<OperationView>() with instance(this@OperationActivity)
        import(operationPresenterModule)
    }

    private val presenter: OperationPresenter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.requestPermissionsIfNeeded(this)

        toggleActivate.setOnCheckedChangeListener { _, isTurnedOn -> presenter.onToggleActivate(isTurnedOn, this) }
        switchStartup.setOnCheckedChangeListener { _, isTurnedOn -> presenter.onToggleRunOnStartup(isTurnedOn) }
    }

    override fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 839)
    }

    override fun toggleActivateIsChecked(isTurnedOn: Boolean) {
        toggleActivate.isChecked = isTurnedOn
    }

    override fun toggleStartupIsChecked(isTurnedOn: Boolean) {
        switchStartup.isChecked = isTurnedOn
    }

}
