package de.jkaumanns.pericontrol.view.validation;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Joerg on 18.09.2016.
 */
public class DeviceNameValidator extends TextValidator {

    public DeviceNameValidator(EditText textView) {
        super(textView);
    }

    @Override
    void validate(EditText textView) {
        if (TextUtils.isEmpty(textView.getText().toString())) {
            textView.setError(textView.getHint() + " can not be empty.");
        } else if (textView.getText().toString().length() > 20) {
            textView.setError(textView.getHint() + " can not be longer than 20 chars.");
        } else if (!textView.getText().toString().matches("[a-zA-Z0-9]+")) {
            textView.setError(textView.getHint() + " allowed chars are: a to z ; A to Z ; 0 to 9.");
        } else {
            textView.setError(null);
        }
    }
}
