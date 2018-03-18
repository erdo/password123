package co.early.password123.ui.passwordchooser

import android.text.Editable
import android.text.TextWatcher

import co.early.asaf.core.Affirm
import co.early.pwned.Pwned


class TextWatcherCheckPassword(pwned: Pwned) : TextWatcher {

    private val pwned: Pwned

    init {
        this.pwned = Affirm.notNull(pwned)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        pwned.setPasswordToCheck(s.toString())
    }

}

