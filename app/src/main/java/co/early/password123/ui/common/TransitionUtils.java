package co.early.password123.ui.common;

import android.app.Activity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;


public class TransitionUtils {

    private static Slide slideRightEdge = new Slide();
    private static Slide slideLeftEdge = new Slide();
    private static Fade fade = new Fade();
    private static Slide slide = new Slide();


    static {
        slideRightEdge.setSlideEdge(Gravity.RIGHT);
        slideLeftEdge.setSlideEdge(Gravity.LEFT);

        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
     //   fade.excludeTarget(R.id.toolbar, true);

        slideRightEdge.excludeTarget(android.R.id.statusBarBackground, true);
        slideRightEdge.excludeTarget(android.R.id.navigationBarBackground, true);
     //   slideRightEdge.excludeTarget(R.id.toolbar, true);

        slideLeftEdge.excludeTarget(android.R.id.statusBarBackground, true);
        slideLeftEdge.excludeTarget(android.R.id.navigationBarBackground, true);
     //   slideLeftEdge.excludeTarget(R.id.toolbar, true);
    }


    public static void postponeTransition(final Activity activity) {

        // Postpone the transition until the window's decor view has
        // finished its layout. Prevents the action bar from flashing
        // during a transition animation

        activity.postponeEnterTransition();

        final View decor = activity.getWindow().getDecorView();
        decor.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                decor.getViewTreeObserver().removeOnPreDrawListener(this);
                activity.startPostponedEnterTransition();
                return true;
            }
        });
    }


    public static void setupTopLevelActivityTransitions(Window window) {

        //window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        window.setEnterTransition(fade);
        window.setReturnTransition(fade);
        window.setExitTransition(null);
        window.setReenterTransition(null);

        window.setSharedElementEnterTransition(slide);
        window.setSharedElementReturnTransition(slide);
        window.setSharedElementExitTransition(slide);
        window.setSharedElementReenterTransition(slide);

//        window.setAllowEnterTransitionOverlap(false);
//        window.setAllowReturnTransitionOverlap(false);

    }

    public static void setupSubLevelActivityTransitions(Window window) {

        //window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        window.setEnterTransition(slideRightEdge);
        window.setReturnTransition(slideRightEdge);
        window.setExitTransition(slideLeftEdge);
        window.setReenterTransition(slideLeftEdge);

        window.setSharedElementEnterTransition(slide);
        window.setSharedElementReturnTransition(slide);
        window.setSharedElementExitTransition(slide);
        window.setSharedElementReenterTransition(slide);

//        window.setAllowEnterTransitionOverlap(false);
//        window.setAllowReturnTransitionOverlap(false);

    }

    public static void setupNoActivityTransitions(Window window) {

        //window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        window.setEnterTransition(null);
        window.setReturnTransition(null);
        window.setExitTransition(null);
        window.setReenterTransition(null);

        window.setSharedElementEnterTransition(null);
        window.setSharedElementReturnTransition(null);
        window.setSharedElementExitTransition(null);
        window.setSharedElementReenterTransition(null);

//        window.setAllowEnterTransitionOverlap(false);
//        window.setAllowReturnTransitionOverlap(false);

    }

}
