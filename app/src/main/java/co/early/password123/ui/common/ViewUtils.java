package co.early.password123.ui.common;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;


public class ViewUtils {

    public static Activity getActivityFromContext(Context context) {

        if (context instanceof Activity) {//this maybe a context from a view hosted in a regular fragment for example
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {//this maybe a context from a view hosted in a dialogfragment for example
            return (Activity) ((ContextWrapper) context).getBaseContext();
        } else {//some other kind of context
            return null;
        }
    }
}