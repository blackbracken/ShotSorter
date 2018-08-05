package black.bracken.shotsorter.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import black.bracken.shotsorter.R;
import black.bracken.shotsorter.ShotSorter;

/**
 * @author BlackBracken
 */
public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShotSorter.startServiceIfNot(this);

        setContentView(R.layout.activity_main);
    }

}
