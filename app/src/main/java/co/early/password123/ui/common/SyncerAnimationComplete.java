package co.early.password123.ui.common;


import android.animation.Animator;
import android.os.Handler;
import android.view.View;

import co.early.asaf.core.Affirm;
import co.early.asaf.core.ui.SyncableView;


public class SyncerAnimationComplete implements Animator.AnimatorListener {

    private final SyncableView syncableView;
    private final View[] viewsToReset;


    public SyncerAnimationComplete(SyncableView syncableView, View... viewsToReset) {
        this.syncableView = Affirm.notNull(syncableView);
        this.viewsToReset = Affirm.notNull(viewsToReset);
    }

    public SyncerAnimationComplete(SyncableView syncableView) {
        this.syncableView = Affirm.notNull(syncableView);
        this.viewsToReset = new View[0];
    }



    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {

        //otherwise when we draw, the animation is still "running" on older android
        new Handler().postDelayed(() -> {

            for (View view : viewsToReset) {
                view.setAlpha(1.0f);
                view.setRotation(0f);
            }
            syncableView.syncView();

        }, 10);//

    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
