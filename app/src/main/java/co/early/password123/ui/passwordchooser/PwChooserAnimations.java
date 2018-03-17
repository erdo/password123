package co.early.password123.ui.passwordchooser;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import co.early.asaf.core.callbacks.SuccessCallBack;
import co.early.asaf.core.ui.SyncableView;
import co.early.asaf.ui.SyncTrigger;
import co.early.password123.feature.networkmonitor.NetworkState;
import co.early.password123.ui.common.ViewUtils;
import co.early.password123.ui.common.anim.CustomEasing;
import co.early.password123.ui.common.anim.SyncerAnimationComplete;
import co.early.password123.ui.common.widgets.LockedKeyboardEditText;
import co.early.pwned.Pwned;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN;


public class PwChooserAnimations {

    //triggered automatically
    private final AnimatorSet networkGoneAnimSet = new AnimatorSet();
    private final AnimatorSet networkBackAnimSet = new AnimatorSet();
    private final AnimatorSet showBusyAnimSet = new AnimatorSet();
    private final AnimatorSet hideBusyAnimSet = new AnimatorSet();
    private final AnimatorSet showInfoAnimSet = new AnimatorSet();
    private final AnimatorSet hideInfoAnimSet = new AnimatorSet();
    private final AnimatorSet removeLoadingBarAnimSet = new AnimatorSet();
    private final AnimatorSet jiggleAnimSet = new AnimatorSet();

    private final SyncTrigger networkGoneTrigger;
    private final SyncTrigger networkBackTrigger;
    private final SyncTrigger showBusyTrigger;
    private final SyncTrigger hideBusyTrigger;
    private final SyncTrigger showInfoTrigger;
    private final SyncTrigger hideInfoTrigger;
    private final SyncTrigger removeLoadingBarTrigger;
    private final SyncTrigger jiggleTrigger;

    private final float jiggleAngle1 = 50f;
    private final float jiggleAngle2 = 20f;
    private final long searchingTransitionAnimDuration = 400;
    private final long networkGoneAnimDuration = 700;
    private final long networkBackAnimDuration = 400;
    private final long clearScreenDuration = 400;

    private final int screenWidthPx;
    private final int screenHeightPx;
    private final float densityScalingFactor;


    public PwChooserAnimations(View netWarnIcon,
                               View netWarnBgd,
                               View infoIcon,
                               View busyIcon,
                               View initProgress,
                               TextView warningText,
                               TextView warningDetail,
                               ImageView cloudIcon,
                               SyncableView syncableView,
                               NetworkState networkState,
                               Pwned pwned,
                               int screenWidthPx,
                               int screenHeightPx,
                               float densityScalingFactor) {

        this.screenWidthPx = screenWidthPx;
        this.screenHeightPx = screenHeightPx;
        this.densityScalingFactor = densityScalingFactor;

        networkGoneTrigger = networkGoneTrigger(networkGoneAnimSet, netWarnIcon, netWarnBgd, syncableView, networkState);
        networkBackTrigger = networkBackTrigger(networkBackAnimSet, netWarnIcon, netWarnBgd, syncableView, networkState);
        showBusyTrigger = showBusyTrigger(showBusyAnimSet, busyIcon, syncableView, pwned);
        hideBusyTrigger = hideBusyTrigger(hideBusyAnimSet, busyIcon, syncableView, pwned);
        showInfoTrigger = showInfoIconTrigger(showInfoAnimSet, infoIcon, syncableView, pwned);
        hideInfoTrigger = hideInfoIconTrigger(hideInfoAnimSet, infoIcon, syncableView, pwned);
        removeLoadingBarTrigger = setupRemoveLoadingBarTrigger(removeLoadingBarAnimSet, initProgress, syncableView, pwned);
        jiggleTrigger = jiggleTrigger(jiggleAnimSet, infoIcon, netWarnIcon, syncableView);

        ViewUtils.allowAnimationOutsideParent(infoIcon);
        ViewUtils.allowAnimationOutsideParent(warningText);
        ViewUtils.allowAnimationOutsideParent(warningDetail);
        ViewUtils.allowAnimationOutsideParent(cloudIcon);
    }

    private SyncTrigger networkGoneTrigger(
            final AnimatorSet animatorSet,
            final View netWarnIcon,
            final View backgroundWarn,
            final SyncableView syncableView,
            final NetworkState networkState){

        animatorSet.setDuration(networkGoneAnimDuration);

        ObjectAnimator bounceDownObjAnimator = ObjectAnimator.ofFloat(netWarnIcon, "translationY", -screenHeightPx/2, 0);
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

        animatorSet.setDuration(networkBackAnimDuration);

        ObjectAnimator flopUpObjAnimator = ObjectAnimator.ofFloat(netWarnIcon, "translationY", 0, -screenHeightPx/2);
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
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(cloudIcon, "rotation", -jiggleAngle1, jiggleAngle1,
                        -jiggleAngle1, jiggleAngle2, -jiggleAngle2, 0f),
                ObjectAnimator.ofFloat(infoIcon, "rotation", -jiggleAngle1, jiggleAngle1,
                        -jiggleAngle1, jiggleAngle2, -jiggleAngle2, 0f));
        animatorSet.addListener(new SyncerAnimationComplete(syncableView, cloudIcon, infoIcon));//onAnimationEnd() -> syncView()


        return new SyncTrigger(() -> {
            animatorSet.start();
        }, () -> true);
    }

    public void checkAll(){
        networkGoneTrigger.checkLazy();
        networkBackTrigger.checkLazy();
        showBusyTrigger.checkLazy();
        hideBusyTrigger.checkLazy();
        showInfoTrigger.checkLazy();
        hideInfoTrigger.checkLazy();
        removeLoadingBarTrigger.checkLazy();
        jiggleTrigger.check(); //not lazy, we want the animation triggered each time the view is recreated
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

    // We're regretably doing it this way as Activity Transitions appear to be a disaster if you go
    // Activity A -> Activity B -> *rotate screen* -> back to Activity A
    public void infoIconClicked(
            final ViewGroup topLevelView,
            final View infoIcon,
            final View busySpinner,
            final LockedKeyboardEditText editText,
            final View eyeView,
            final View warningText,
            final View warningDetail,
            final View cloudIcon,
            final View transitionBackground,
            final SuccessCallBack successCallBack){

        //current location of infoIcon
        infoIcon.setRotation(0f);//if it is in the middle of jiggling, the location is wrong
        Rect offsetViewBounds = new Rect();
        infoIcon.getDrawingRect(offsetViewBounds);
        // calculates the relative coordinates to the parent
        topLevelView.offsetDescendantRectToMyCoords(infoIcon, offsetViewBounds);
        int x = offsetViewBounds.left;
        int y = offsetViewBounds.top;

        int targetX = (int)(((float)screenWidthPx - ((float)infoIcon.getWidth())*1f)/2f);//1.23 because of the scale difference
        int targetY = (int)((float)8*densityScalingFactor);//top padding in About screen is 10dp so I don't know why 8 works better here

        //where we want to be with infoIcon
        int xChange = targetX - x;
        int yChange = targetY - y;

        infoIcon.setEnabled(false);
        // because if a user closes the keyboard by pressing back at this point,
        // all the animations are borked
        editText.setKeyboardLockedOpen(true);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(clearScreenDuration);
        animatorSet.setInterpolator(CustomEasing.innieOutie);

        ObjectAnimator warningDetailAnimator =  ObjectAnimator.ofFloat(warningDetail, "translationX", 0, -screenWidthPx);
        warningDetailAnimator.setStartDelay(0);
        ObjectAnimator warningTextAnimator =  ObjectAnimator.ofFloat(warningText, "translationX", 0, -screenWidthPx);
        warningTextAnimator.setStartDelay(50);
        ObjectAnimator editTextAnimator = ObjectAnimator.ofFloat(editText, "translationX", 0, -screenWidthPx);
        editTextAnimator.setStartDelay(100);
        ObjectAnimator eyeViewAnimator = ObjectAnimator.ofFloat(eyeView, "translationX", 0, -screenWidthPx);
        eyeViewAnimator.setStartDelay(100);
        ObjectAnimator cloudAnimator = ObjectAnimator.ofFloat(cloudIcon, "translationX", 0, screenWidthPx);
        cloudAnimator.setStartDelay(150);
        ObjectAnimator infoAnimator1 = ObjectAnimator.ofFloat(infoIcon, "translationY", 0, yChange);
        infoAnimator1.setStartDelay(250);
        ObjectAnimator infoAnimator2 = ObjectAnimator.ofFloat(infoIcon, "translationX", 0, xChange);
        infoAnimator2.setStartDelay(250);
        ObjectAnimator infoAnimator3 = ObjectAnimator.ofFloat(infoIcon, "scaleX", 1f, 0.7f, 0.7f, 1.1f);
        infoAnimator3.setStartDelay(250);
        ObjectAnimator infoAnimator4 = ObjectAnimator.ofFloat(infoIcon, "scaleY", 1f, 0.7f, 0.7f, 1.1f);
        infoAnimator4.setStartDelay(250);
        ObjectAnimator backgroundAnimator = ObjectAnimator.ofFloat(transitionBackground, "alpha", 0, 1f);
        backgroundAnimator.setStartDelay(250);


        animatorSet.playTogether(
                ObjectAnimator.ofFloat(busySpinner, "translationX", 0, -screenWidthPx),
                warningDetailAnimator,
                warningTextAnimator,
                editTextAnimator,
                eyeViewAnimator,
                cloudAnimator,
                infoAnimator1,
                infoAnimator2,
                infoAnimator3,
                infoAnimator4,
                backgroundAnimator);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                successCallBack.success();

                //yukky reset stuff
                new Handler().postDelayed(() -> {

                    infoIcon.setEnabled(true);
                    editText.setKeyboardLockedOpen(false);

                    infoIcon.setTranslationX(0f);
                    infoIcon.setTranslationY(0f);
                    infoIcon.setScaleX(1f);
                    infoIcon.setScaleY(1f);
                    editText.setTranslationX(0f);
                    eyeView.setTranslationX(0f);
                    cloudIcon.setTranslationX(0f);
                    busySpinner.setTranslationX(0f);
                    warningText.setTranslationX(0f);
                    warningDetail.setTranslationX(0f);
                    transitionBackground.setAlpha(0f);

                }, 1000);//need to allow time for the about activity to start

            }
        });
        animatorSet.start();
    }

}

