package black.bracken.shotsorter.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.util.ContextUtil;

/**
 * @author BlackBracken
 */
public final class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_STORAGE_CODE = 8931;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!ContextUtil.hasPermission(this, Manifest.permission_group.STORAGE)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STORAGE_CODE);
        }
    }

}
