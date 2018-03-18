package co.early.password123.feature.passwordvisibility

import co.early.asaf.core.WorkMode
import co.early.asaf.core.observer.ObservableImp


class PasswordVisibility(notificationMode: WorkMode) : ObservableImp(notificationMode) {

    var isVisible = false
        set(visible) {
            field = visible
            notifyObservers()
        }

    fun toggleVisibility() {
        isVisible = !isVisible
        notifyObservers()
    }

}
