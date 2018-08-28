package black.bracken.shotsorter.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Switch
import android.widget.ToggleButton
import black.bracken.shotsorter.R
import black.bracken.shotsorter.constant.PreferencesKeys
import black.bracken.shotsorter.service.SortService
import black.bracken.shotsorter.util.ContextUtil

/**
 * @author BlackBracken
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // requests permission to read & write storage
        if (!ContextUtil.hasPermission(this, Manifest.permission_group.STORAGE)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), CODE_PERMISSION_STORAGE)
        }

        val preferences = getSharedPreferences(PreferencesKeys.ROOT, Context.MODE_PRIVATE)

        val toggleService = findViewById<ToggleButton>(R.id.toggle_service)
        toggleService.isChecked = SortService.isRunning
        toggleService.setOnCheckedChangeListener { button, isTurnedOn ->
            if (isTurnedOn) {
                SortService.start(this)
            } else {
                this.stopService(Intent(this, SortService::class.java))
            }
        }

        val switchStartup = findViewById<Switch>(R.id.switch_startup)
        switchStartup.isChecked = preferences.getBoolean(PreferencesKeys.RUN_ON_STARTUP, false)
        switchStartup.setOnCheckedChangeListener { button, isTurnedOn ->
            val editor = preferences.edit()
            editor.putBoolean(PreferencesKeys.RUN_ON_STARTUP, isTurnedOn)
            editor.apply()
        }
    }

    companion object {

        private val CODE_PERMISSION_STORAGE = 8931
    }

}
