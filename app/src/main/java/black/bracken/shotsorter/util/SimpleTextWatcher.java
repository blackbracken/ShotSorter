package black.bracken.shotsorter.util;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * @author BlackBracken
 */
public interface SimpleTextWatcher extends TextWatcher {

    default void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

    default void afterTextChanged(Editable editable) {}

}
