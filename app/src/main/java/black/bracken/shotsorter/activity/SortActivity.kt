package black.bracken.shotsorter.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import black.bracken.shotsorter.ImageManipulator
import black.bracken.shotsorter.R
import black.bracken.shotsorter.service.SortService
import black.bracken.shotsorter.util.IntUtil
import black.bracken.shotsorter.util.SimpleTextWatcher
import lib.folderpicker.FolderPicker
import java.io.File
import java.io.FileNotFoundException

/**
 * @author BlackBracken
 */
class SortActivity : Activity() {

    private var uri: Uri? = null
    private var manipulator: ImageManipulator? = null

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_sort)

        this.uri = intent.getParcelableExtra(SortService.URI_KEY)
        this.manipulator = ImageManipulator(File(uri!!.path))

        val layout = findViewById<ConstraintLayout>(R.id.layout_sort)
        try {
            layout.background = Drawable.createFromStream(contentResolver.openInputStream(uri!!), uri!!.toString())
        } catch (ex: FileNotFoundException) {
            ex.printStackTrace()
        }

        val editDelaySeconds = findViewById<EditText>(R.id.edit_delay_seconds)
        val defaultKeyListener = editDelaySeconds.keyListener
        editDelaySeconds.keyListener = null
        editDelaySeconds.addTextChangedListener({ charSequence, start, count, after -> manipulator!!.setSecondsDeletingLater(IntUtil.parseIntOrDefault(charSequence.toString(), 0)) } as SimpleTextWatcher
        )

        val switchAutoRemove = findViewById<Switch>(R.id.switch_delay_remove)
        switchAutoRemove.setOnCheckedChangeListener { button, isTurnedOn ->
            editDelaySeconds.keyListener = if (isTurnedOn) defaultKeyListener else null

            if (!isTurnedOn) {
                manipulator!!.setSecondsDeletingLater(0)
            }
        }

        val buttonChangeDestination = findViewById<Button>(R.id.button_change_destination)
        buttonChangeDestination.setOnClickListener { view ->
            val fileSelector = Intent(this, FolderPicker::class.java)
            startActivityForResult(fileSelector, CODE_FOLDER_SELECT)
        }

        val buttonExit = findViewById<Button>(R.id.button_exit)
        buttonExit.setOnClickListener { view ->
            manipulator!!.manipulate(this)
            finish()
        }

        val buttonExitWithoutApplying = findViewById<Button>(R.id.button_exit_without_applying)
        buttonExitWithoutApplying.setOnClickListener { view -> finish() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode != Activity.RESULT_OK) return

        when (requestCode) {
            CODE_FOLDER_SELECT -> manipulator!!.setDestinationAsFolder(File(intent.extras!!.getString("data")!!))
        }
    }

    companion object {

        private val CODE_FOLDER_SELECT = 8512
    }

}
