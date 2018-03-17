package co.early.password123.ui.common.widgets;


import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import co.early.password123.ui.common.ViewUtils;

/**
 * We don't want the keyboard closing while our transition animation is running as
 * it messes up the positions, so we use this instead of a regular edittext
 * so that we can temporarily dissable the back to close keyboard action
 */
public class LockedKeyboardEditText extends android.support.v7.widget.AppCompatEditText{


    private boolean keyboardLockedOpen = false;


    public LockedKeyboardEditText(Context context) {
        super(context);
    }

    public LockedKeyboardEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockedKeyboardEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyboardLockedOpen) {
            ViewUtils.getActivity(getContext()).onBackPressed();
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }


    public boolean isKeyboardLockedOpen() {
        return keyboardLockedOpen;
    }

    public void setKeyboardLockedOpen(boolean keyboardLockedOpen) {
        this.keyboardLockedOpen = keyboardLockedOpen;
    }
}
