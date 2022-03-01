package pl.first.firstjava;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedRuntimeException extends RuntimeException {
    public LocalizedRuntimeException(String messageKey) {
        super(messageKey);
    }

    @Override
    public String getMessage() {
        return this.getLocalizedMessage();
    }

    @Override
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("bundle2", Locale.getDefault());
        return bundle.getString(super.getMessage());
    }
}
