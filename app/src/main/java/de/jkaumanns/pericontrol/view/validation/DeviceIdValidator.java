package de.jkaumanns.pericontrol.view.validation;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Joerg on 18.09.2016.
 */
public class DeviceIdValidator extends TextValidator {

    public DeviceIdValidator(EditText textView) {
        super(textView);
    }

    @Override
    void validate(EditText textView) {
        if (TextUtils.isEmpty(textView.getText().toString())) {
            textView.setError(textView.getHint() + " has to be between 1 and 254.");
        } else if (Integer.parseInt(textView.getText().toString()) < 1 || Integer.parseInt(textView.getText().toString()) > 254) {
            textView.setError(textView.getHint() + " has to be between 1 and 254.");
        } else {
            textView.setError(null);
        }
    }
}
