package co.early.password123.ui.common;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import co.early.asaf.core.Affirm;
import co.early.pwned.Pwned;



public class TextWatcherCheckPassword implements TextWatcher {

    private final Pwned pwned;
    private final EditText editText;

    public TextWatcherCheckPassword(EditText editText, Pwned pwned) {
        this.editText = Affirm.notNull(editText);
        this.pwned = Affirm.notNull(pwned);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        pwned.setPasswordToCheck(editText.getText().toString());
    }

}
