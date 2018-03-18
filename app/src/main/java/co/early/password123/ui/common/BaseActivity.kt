package co.early.password123.ui.common


import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import co.early.password123.CustomApp
import co.early.password123.R


abstract class BaseActivity : AppCompatActivity() {

    var currentActivityScreenWidthPx: Int = 0
        private set
    var currentActivityScreenHeightPx: Int = 0
        private set

    abstract val fragmentInstance: Fragment
    abstract val fragmentTag: String

    val densityScalingFactor: Float
        get() = resources.displayMetrics.density

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        CustomApp.init()

        setupScreenDimensions()

        setContentView(R.layout.common_activity_base)

        if (savedInstanceState == null) {
            setFragment(fragmentInstance, fragmentTag)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun setFragment(fragment: Fragment, fragmentTag: String) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(
                R.id.content_main,
                fragment,
                fragmentTag)
        fragmentTransaction.commitAllowingStateLoss()
    }

    private fun setupScreenDimensions() {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)
        currentActivityScreenWidthPx = size.x
        currentActivityScreenHeightPx = size.y
    }

}