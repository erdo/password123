package co.early.password123.ui.common

import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup


object ViewUtils {

    fun getActivity(context: Context): BaseActivity? {

        return context as? BaseActivity ?: if (context is ContextWrapper) {//this maybe a context from a view hosted in a dialogfragment for example
            context.baseContext as BaseActivity
        } else {//some other kind of context
            null
        }
    }

    fun allowAnimationOutsideParent(v: View) {
        var v = v
        while (v.parent != null && v.parent is ViewGroup) {
            val viewGroup = v.parent as ViewGroup
            viewGroup.clipChildren = false
            viewGroup.clipToPadding = false
            v = viewGroup
        }
    }
}