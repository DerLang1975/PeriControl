package de.jkaumanns.pericontrol.view.validation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Joerg on 18.09.2016.
 */
public abstract class TextValidator implements TextWatcher {

    private EditText textView;

    public TextValidator(EditText textView) {
        this.textView = textView;
    }

    abstract void validate(EditText textView);

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        validate(textView);
    }

}
