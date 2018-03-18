package co.early.password123.ui.common.anim


import android.animation.Animator
import android.os.Handler
import android.view.View

import co.early.asaf.core.Affirm
import co.early.asaf.core.ui.SyncableView


class SyncerAnimationComplete : Animator.AnimatorListener {

    private val syncableView: SyncableView
    private val viewsToReset: Array<out View>


    constructor(syncableView: SyncableView, vararg viewsToReset: View) {
        this.syncableView = Affirm.notNull(syncableView)
        this.viewsToReset = Affirm.notNull(viewsToReset)
    }

    constructor(syncableView: SyncableView) {
        this.syncableView = Affirm.notNull(syncableView)
        this.viewsToReset = emptyArray()
    }


    override fun onAnimationStart(animation: Animator) {

    }

    override fun onAnimationEnd(animation: Animator) {

        //otherwise when we draw, the animation is still "running" on older android
        Handler().postDelayed({

            for (view in viewsToReset) {
                view.alpha = 1.0f
                view.rotation = 0f
            }
            syncableView.syncView()

        }, 10)//

    }

    override fun onAnimationCancel(animation: Animator) {

    }

    override fun onAnimationRepeat(animation: Animator) {

    }
}
