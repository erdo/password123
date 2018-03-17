package co.early.password123.ui.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;
import android.view.ViewGroup;


public class ViewUtils {

    public static BaseActivity getActivity(Context context) {

        if (context instanceof BaseActivity) {//this maybe a context from a view hosted in a regular fragment for example
            return (BaseActivity) context;
        } else if (context instanceof ContextWrapper) {//this maybe a context from a view hosted in a dialogfragment for example
            return (BaseActivity) ((ContextWrapper) context).getBaseContext();
        } else {//some other kind of context
            return null;
        }
    }

    public static void allowAnimationOutsideParent(View v) {
        while (v.getParent() != null && v.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) v.getParent();
            viewGroup.setClipChildren(false);
            viewGroup.setClipToPadding(false);
            v = viewGroup;
        }
    }
}