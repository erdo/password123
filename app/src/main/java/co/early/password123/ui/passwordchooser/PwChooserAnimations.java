package co.early.password123.ui.passwordchooser;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import co.early.asaf.core.ui.SyncableView;
import co.early.asaf.ui.SyncTrigger;
import co.early.password123.feature.networkmonitor.NetworkState;
import co.early.password123.ui.common.SyncerAnimationComplete;
import co.early.password123.ui.common.anim.CustomEasing;
import co.early.pwned.Pwned;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN;


public class PwChooserAnimations {

    final AnimatorSet networkGoneAnimSet = new AnimatorSet();
    final AnimatorSet networkBackAnimSet = new AnimatorSet();
    final AnimatorSet showBusyAnimSet = new AnimatorSet();
    final AnimatorSet hideBusyAnimSet = new AnimatorSet();
    final AnimatorSet showInfoAnimSet = new AnimatorSet();
    final AnimatorSet hideInfoAnimSet = new AnimatorSet();
    final AnimatorSet removeLoadingBarAnimSet = new AnimatorSet();
    final AnimatorSet jiggleAnimSet = new AnimatorSet();

    final SyncTrigger networkGoneTrigger;
    final SyncTrigger networkBackTrigger;
    final SyncTrigger showBusyTrigger;
    final SyncTrigger hideBusyTrigger;
    final SyncTrigger showInfoTrigger;
    final SyncTrigger hideInfoTrigger;
    final SyncTrigger removeLoadingBarTrigger;
    final SyncTrigger jiggleTrigger;

    private final float jiggleAngle1 = 50f;
    private final float jiggleAngle2 = 20f;
    private long searchingTransitionAnimDuration = 400;
    private long networkChangeAnimDuration = 400;
    private long networkChangeTravelDistance = 200;


    public PwChooserAnimations(View netWarnIcon,
                               View netWarnBgd,
                               View infoIcon,
                               View busyIcon,
                               View initProgress,
                               SyncableView syncableView,
                               NetworkState networkState,
                               Pwned pwned) {

        networkGoneTrigger = networkGoneTrigger(networkGoneAnimSet, netWarnIcon, netWarnBgd, syncableView, networkState);
        networkBackTrigger = networkBackTrigger(networkBackAnimSet, netWarnIcon, netWarnBgd, syncableView, networkState);
        showBusyTrigger = showBusyTrigger(showBusyAnimSet, busyIcon, syncableView, pwned);
        hideBusyTrigger = hideBusyTrigger(hideBusyAnimSet, busyIcon, syncableView, pwned);
        showInfoTrigger = showInfoIconTrigger(showInfoAnimSet, infoIcon, syncableView, pwned);
        hideInfoTrigger = hideInfoIconTrigger(hideInfoAnimSet, infoIcon, syncableView, pwned);
        removeLoadingBarTrigger = setupRemoveLoadingBarTrigger(removeLoadingBarAnimSet, initProgress, syncableView, pwned);
        jiggleTrigger = jiggleTrigger(jiggleAnimSet, infoIcon, netWarnIcon, syncableView);
    }

    private SyncTrigger networkGoneTrigger(
            final AnimatorSet animatorSet,
            final View netWarnIcon,
            final View backgroundWarn,
            final SyncableView syncableView,
            final NetworkState networkState){

        animatorSet.setDuration(networkChangeAnimDuration);

        ObjectAnimator bounceDownObjAnimator = ObjectAnimator.ofFloat(netWarnIcon, "translationY", -networkChangeTravelDistance, 0);
        bounceDownObjAnimator.setInterpolator(CustomEasing.bounceDown);
        ObjectAnimator backgroundFadeObjAnimator = ObjectAnimator.ofFloat(backgroundWarn, "alpha", 0f, 1f);

        animatorSet.playTogether(
                bounceDownObjAnimator,
                backgroundFadeObjAnimator);
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, backgroundWarn, netWarnIcon));//onAnimationEnd() -> resetAlpha values, then call syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            netWarnIcon.setVisibility(VISIBLE);
            backgroundWarn.setVisibility(VISIBLE);
            animatorSet.start();
        }, () -> !networkState.isConnected());
    }

    private SyncTrigger networkBackTrigger(
            final AnimatorSet animatorSet,
            final View netWarnIcon,
            final View backgroundWarn,
            final SyncableView syncableView,
            final NetworkState networkState){

        animatorSet.setDuration(networkChangeAnimDuration);

        ObjectAnimator flopUpObjAnimator = ObjectAnimator.ofFloat(netWarnIcon, "translationY", 0, -networkChangeTravelDistance);
        flopUpObjAnimator.setInterpolator(CustomEasing.flopUp);
        ObjectAnimator backgroundFadeObjAnimator = ObjectAnimator.ofFloat(backgroundWarn, "alpha", 1f, 0f);

        animatorSet.playTogether(
                flopUpObjAnimator,
                backgroundFadeObjAnimator);
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, backgroundWarn, netWarnIcon));//onAnimationEnd() -> resetAlpha values, then call syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            netWarnIcon.setVisibility(VISIBLE);
            backgroundWarn.setVisibility(VISIBLE);
            animatorSet.start();
        }, () -> networkState.isConnected());
    }

    private SyncTrigger showBusyTrigger(
            final AnimatorSet animatorSet,
            final View busySpinner,
            final SyncableView syncableView,
            final Pwned pwned){

        animatorSet.setDuration(searchingTransitionAnimDuration);
        animatorSet.playTogether(ObjectAnimator.ofFloat(busySpinner, "alpha", 0f, 1f));
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, busySpinner));//onAnimationEnd() -> resetAlpha values, then call syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            busySpinner.setVisibility(VISIBLE);
            animatorSet.start();
        }, () -> pwned.isBusy());
    }


    private SyncTrigger hideBusyTrigger(
            final AnimatorSet animatorSet,
            final View busySpinner,
            final SyncableView syncableView,
            final Pwned pwned){

        animatorSet.setDuration(searchingTransitionAnimDuration);
        animatorSet.playTogether(ObjectAnimator.ofFloat(busySpinner, "alpha", 1f, 0f));
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, busySpinner));//onAnimationEnd() -> resetAlpha values, then call syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            busySpinner.setVisibility(VISIBLE);
            animatorSet.start();
        }, () -> !pwned.isBusy());
    }


    private SyncTrigger hideInfoIconTrigger(
            final AnimatorSet animatorSet,
            final View infoIcon,
            final SyncableView syncableView,
            final Pwned pwned){

        animatorSet.setDuration(searchingTransitionAnimDuration);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(infoIcon, "alpha", 1f, 0f),
                ObjectAnimator.ofFloat(infoIcon, "rotation", 0f, 90f)
        );
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, infoIcon));//onAnimationEnd() -> resetAlpha values, then call syncView()


        return new SyncTrigger(() -> {
            animatorSet.start();
        }, () -> pwned.isBusy());
    }


    private SyncTrigger showInfoIconTrigger(
            final AnimatorSet animatorSet,
            final View infoIcon,
            final SyncableView syncableView,
            final Pwned pwned){

        animatorSet.setDuration(searchingTransitionAnimDuration);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(infoIcon, "alpha", 0f, 1f),
                ObjectAnimator.ofFloat(infoIcon, "rotation", -90f, 0f)
        );
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, infoIcon));//onAnimationEnd() -> resetAlpha values, then call syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            infoIcon.setVisibility(!pwned.isBusy() && pwned.getPwnedResult().pwnedState != UNKNOWN ? VISIBLE : INVISIBLE);
            animatorSet.start();
        }, () -> !pwned.isBusy());
    }


    private SyncTrigger setupRemoveLoadingBarTrigger(
            final AnimatorSet animatorSet,
            final View initProgress,
            final SyncableView syncableView,
            final Pwned pwned){

        animatorSet.setDuration(700);
        animatorSet.playTogether(ObjectAnimator.ofFloat(initProgress, "alpha", 1f, 0f));
        animatorSet.addListener(new SyncerAnimationComplete(syncableView));//onAnimationEnd() -> syncView()

        return new SyncTrigger(() -> {
            // temporarily adjust visibility before running animation, syncView() gets
            // called at the end of the animation anyway to put everything back to how it should be
            initProgress.setVisibility(VISIBLE);
            animatorSet.start();
        }, () -> pwned.offlineDataLoadingComplete());
    }


    private SyncTrigger jiggleTrigger(
            final AnimatorSet animatorSet,
            final View infoIcon,
            final View cloudIcon,
            final SyncableView syncableView){

        animatorSet.setDuration(1000);
        animatorSet.removeAllListeners();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(cloudIcon, "rotation", -jiggleAngle1, jiggleAngle1,
                        -jiggleAngle1, jiggleAngle2, -jiggleAngle2, 0f),
                ObjectAnimator.ofFloat(infoIcon, "rotation", -jiggleAngle1, jiggleAngle1,
                        -jiggleAngle1, jiggleAngle2, -jiggleAngle2, 0f));
        animatorSet.start();
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, cloudIcon, infoIcon));//onAnimationEnd() -> syncView()


        return new SyncTrigger(() -> {
            animatorSet.start();
        }, () -> true);
    }


    public void checkAllLazy(){
        networkGoneTrigger.checkLazy();
        networkBackTrigger.checkLazy();
        showBusyTrigger.checkLazy();
        hideBusyTrigger.checkLazy();
        showInfoTrigger.checkLazy();
        hideInfoTrigger.checkLazy();
        removeLoadingBarTrigger.checkLazy();
        jiggleTrigger.checkLazy();
    }

    public boolean busyTransitionAnimationsRunning(){
       return showBusyAnimSet.isRunning() || hideBusyAnimSet.isRunning()
                || showInfoAnimSet.isRunning() || hideInfoAnimSet.isRunning();
    }

    public boolean removeLoadingBarAnimRunning(){
        return removeLoadingBarAnimSet.isRunning();
    }

    public boolean networkChangeAnimRunning() {
        return networkGoneAnimSet.isRunning() || networkBackAnimSet.isRunning();
    }

}