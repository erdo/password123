package co.early.password123.feature.passwordvisibility;

import co.early.asaf.core.WorkMode;
import co.early.asaf.core.observer.ObservableImp;


public class PasswordVisibility extends ObservableImp {

    private boolean visible = false;

    public PasswordVisibility(WorkMode notificationMode) {
        super(notificationMode);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        notifyObservers();
    }

    public void toggleVisibility() {
        setVisible(!visible);
        notifyObservers();
    }

}
