package co.early.password123.ui.common.widgets


import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent

import co.early.password123.ui.common.ViewUtils

/**
 * We don't want the keyboard closing while our transition animation is running as
 * it messes up the positions, so we use this instead of a regular edittext
 * so that we can temporarily dissable the back to close keyboard action
 */
class LockedKeyboardEditText : android.support.v7.widget.AppCompatEditText {


    var isKeyboardLockedOpen = false


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && isKeyboardLockedOpen) {
            ViewUtils.getActivity(context)!!.onBackPressed()
            return true
        }
        return super.onKeyPreIme(keyCode, event)
    }
}
