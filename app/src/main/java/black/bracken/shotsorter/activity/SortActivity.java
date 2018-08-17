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
import java.util.Objects;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.service.SortService;

/**
 * @author BlackBracken
 */
public final class SortActivity extends Activity {

    private static final int CODE_FOLDER_SELECT = 8512;

    private Uri uri;
    private File destination = null;

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

        EditText editRemovalSeconds = findViewById(R.id.edit_removal_seconds);
        KeyListener defaultKeyListener = editRemovalSeconds.getKeyListener();

        Switch switchAutoRemove = findViewById(R.id.switch_auto_remove);
        switchAutoRemove.setOnCheckedChangeListener(
                (button, isTurnedOn) -> editRemovalSeconds.setKeyListener(isTurnedOn ? defaultKeyListener : null)
        );

        Button buttonChangeDestination = findViewById(R.id.button_change_destination);
        buttonChangeDestination.setOnClickListener(view -> {
            Intent fileSelector = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            startActivityForResult(fileSelector, CODE_FOLDER_SELECT);
        });

        Button buttonExit = findViewById(R.id.button_exit);
        buttonExit.setOnClickListener(view -> {
        });

        Button buttonExitWithoutApplying = findViewById(R.id.button_exit_without_applying);
        buttonExitWithoutApplying.setOnClickListener(view -> finish());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CODE_FOLDER_SELECT:
                this.destination = new File(Objects.requireNonNull(data.getDataString()));
                break;
        }
    }

}
