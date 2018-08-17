package black.bracken.shotsorter.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.method.KeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.SimpleScreenshotObserver;
import black.bracken.shotsorter.service.SortService;
import black.bracken.shotsorter.util.FileUtil;
import black.bracken.shotsorter.util.SimpleTextWatcher;
import lib.folderpicker.FolderPicker;

/**
 * @author BlackBracken
 */
public final class SortActivity extends Activity {

    private static final int CODE_FOLDER_SELECT = 8512;

    private Uri uri;
    private File destination = null;
    private boolean enablesDelayRemove = false;
    private int delaySecondsToRemove = 0;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sort);

        this.uri = getIntent().getParcelableExtra(SortService.URI_KEY);

        ConstraintLayout layout = findViewById(R.id.layout_sort);
        try {
            layout.setBackground(
                    Drawable.createFromStream(getContentResolver().openInputStream(uri), uri.toString())
            );
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        EditText editDelaySeconds = findViewById(R.id.edit_delay_seconds);
        KeyListener defaultKeyListener = editDelaySeconds.getKeyListener();
        editDelaySeconds.setKeyListener(null);
        editDelaySeconds.addTextChangedListener(
                (SimpleTextWatcher) (charSequence, start, count, after) -> {
                    int seconds;
                    try {
                        seconds = Integer.valueOf(charSequence.toString());
                    } catch (NumberFormatException ex) {
                        seconds = -1;
                    }

                    delaySecondsToRemove = seconds;
                }
        );

        Switch switchAutoRemove = findViewById(R.id.switch_delay_remove);
        switchAutoRemove.setOnCheckedChangeListener(
                (button, isTurnedOn) -> {
                    enablesDelayRemove = isTurnedOn;
                    editDelaySeconds.setKeyListener(isTurnedOn ? defaultKeyListener : null);
                }
        );

        Button buttonChangeDestination = findViewById(R.id.button_change_destination);
        buttonChangeDestination.setOnClickListener(view -> {
            Intent fileSelector = new Intent(this, FolderPicker.class);
            startActivityForResult(fileSelector, CODE_FOLDER_SELECT);
        });

        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(view -> {
            applyToFile();
            finish();
        });

        Button buttonExitWithoutApplying = findViewById(R.id.button_exit_without_applying);
        buttonExitWithoutApplying.setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode != Activity.RESULT_OK) return;

        switch (requestCode) {
            case CODE_FOLDER_SELECT:
                destination = new File(intent.getExtras().getString("data"));
                break;
        }
    }

    @SuppressLint("NewApi")
    private void applyToFile() {
        File screenshot = new File(uri.getPath());

        if (destination != null) {
            File moved = new File(destination.getPath() + File.separator + screenshot.getName());

            try {
                Files.copy(Paths.get(screenshot.toURI()), Paths.get(moved.toURI()));
                FileUtil.deleteImageCertainly(this, screenshot);
                screenshot = moved;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (enablesDelayRemove) {
            File finalScreenshot = screenshot;

            new Handler().postDelayed(() -> {
                FileUtil.deleteImageCertainly(this, finalScreenshot);
                if (finalScreenshot.exists()) {
                    finalScreenshot.delete();
                }
            }, delaySecondsToRemove * 1000);
        }

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(SimpleScreenshotObserver.SCREENSHOT_DIR_PATH)));
    }

}
