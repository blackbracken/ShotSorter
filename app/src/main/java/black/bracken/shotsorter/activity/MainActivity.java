package black.bracken.shotsorter.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Switch;
import android.widget.ToggleButton;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.constant.PreferencesKeys;
import black.bracken.shotsorter.service.SortService;
import black.bracken.shotsorter.util.ContextUtil;

/**
 * @author BlackBracken
 */
public final class MainActivity extends AppCompatActivity {

    private static final int CODE_PERMISSION_STORAGE = 8931;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // requests permission to read & write storage
        if (!ContextUtil.hasPermission(this, Manifest.permission_group.STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, CODE_PERMISSION_STORAGE);
        }

        final SharedPreferences preferences = getSharedPreferences(PreferencesKeys.ROOT, Context.MODE_PRIVATE);

        ToggleButton toggleService = findViewById(R.id.toggle_service);
        toggleService.setChecked(SortService.isRunning());
        toggleService.setOnCheckedChangeListener((button, isTurnedOn) -> {
            if (isTurnedOn) {
                SortService.start(this);
            } else {
                this.stopService(new Intent(this, SortService.class));
            }
        });

        Switch switchStartup = findViewById(R.id.switch_startup);
        switchStartup.setChecked(preferences.getBoolean(PreferencesKeys.RUN_ON_STARTUP, false));
        switchStartup.setOnCheckedChangeListener((button, isTurnedOn) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PreferencesKeys.RUN_ON_STARTUP, isTurnedOn);
            editor.apply();
        });
    }

}
