package de.jkaumanns.pericontrol.view.validation;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Joerg on 18.09.2016.
 */
public class DevicePortTimeoutValidator extends TextValidator {

    public DevicePortTimeoutValidator(EditText textView) {
        super(textView);
    }

    @Override
    void validate(EditText textView) {
        if (TextUtils.isEmpty(textView.getText().toString())) {
            textView.setError(textView.getHint() + " can not be empty.");
        } else if (Integer.parseInt(textView.getText().toString()) < 100 || Integer.parseInt(textView.getText().toString()) > 20000) {
            textView.setError(textView.getHint() + " must be between 100 and 20.000 ms.");
        } else {
            textView.setError(null);
        }
    }
}
