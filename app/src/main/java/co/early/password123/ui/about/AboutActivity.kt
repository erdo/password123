package co.early.password123.ui.about


import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment

import co.early.password123.ui.common.BaseActivity


class AboutActivity : BaseActivity() {


    override val fragmentInstance: Fragment
        get() = AboutFragment.newInstance()

    override val fragmentTag: String
        get() = AboutFragment::class.java.simpleName

    companion object {


        fun start(context: Context) {
            val intent = build(context)
            context.startActivity(intent)
        }

        fun build(context: Context): Intent {
            return Intent(context, AboutActivity::class.java)
        }
    }

}