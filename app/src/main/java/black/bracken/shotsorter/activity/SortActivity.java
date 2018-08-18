package black.bracken.shotsorter.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.method.KeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.io.File;
import java.io.FileNotFoundException;

import black.bracken.shotsorter.ImageManipulator;
import black.bracken.shotsorter.R;
import black.bracken.shotsorter.service.SortService;
import black.bracken.shotsorter.util.IntUtil;
import black.bracken.shotsorter.util.SimpleTextWatcher;
import lib.folderpicker.FolderPicker;

/**
 * @author BlackBracken
 */
public final class SortActivity extends Activity {

    private static final int CODE_FOLDER_SELECT = 8512;

    private Uri uri;
    private ImageManipulator manipulator;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_sort);

        this.uri = getIntent().getParcelableExtra(SortService.URI_KEY);
        this.manipulator = new ImageManipulator(new File(uri.getPath()));

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
        editDelaySeconds.addTextChangedListener((SimpleTextWatcher) (charSequence, start, count, after) ->
                manipulator.setSecondsDeletingLater(IntUtil.parseIntOrDefault(charSequence.toString(), 0))
        );

        Switch switchAutoRemove = findViewById(R.id.switch_delay_remove);
        switchAutoRemove.setOnCheckedChangeListener((button, isTurnedOn) -> {
            editDelaySeconds.setKeyListener(isTurnedOn ? defaultKeyListener : null);

            if (!isTurnedOn) {
                manipulator.setSecondsDeletingLater(0);
            }
        });

        Button buttonChangeDestination = findViewById(R.id.button_change_destination);
        buttonChangeDestination.setOnClickListener(view -> {
            Intent fileSelector = new Intent(this, FolderPicker.class);
            startActivityForResult(fileSelector, CODE_FOLDER_SELECT);
        });

        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(view -> {
            manipulator.manipulate(this);
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
                manipulator.setDestinationAsFolder(new File(intent.getExtras().getString("data")));
                break;
        }
    }

}
