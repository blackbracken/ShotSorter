package black.bracken.shotsorter.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import black.bracken.shotsorter.service.SortService;

/**
 * @author BlackBracken
 */
public final class SortActivity extends Activity {

    private Uri uri;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        this.uri = getIntent().getParcelableExtra(SortService.URI_KEY);

        Toast.makeText(this, "detected screenshot: " + uri.toString(), Toast.LENGTH_SHORT).show();
    }

}
