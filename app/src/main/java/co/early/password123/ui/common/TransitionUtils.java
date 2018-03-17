package co.early.password123.ui.common;

import android.app.Activity;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;


public class TransitionUtils {

    private static Slide slideTopEdge = new Slide();
    private static Slide slideBottomEdge = new Slide();
    private static Fade fade = new Fade();


    static {
        slideTopEdge.setSlideEdge(Gravity.TOP);
        slideBottomEdge.setSlideEdge(Gravity.BOTTOM);

//        fade.excludeTarget(android.R.id.statusBarBackground, true);
//        fade.excludeTarget(R.id.toolbar, true);
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


    //these will only work if you start your activity using ActivityOptions.makeSceneTransitionAnimation
    public static void setupActivityTransitions(Window window) {

        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);

        // material design defaults for A -> B
//        window.setExitTransition(null); // A Exits
//        window.setEnterTransition(fade); // B Enters
//        window.setReturnTransition(fade); // B Exits (Returns to A)
//        window.setReenterTransition(null); // A Enters (Returns from B)


//        window.setExitTransition(null); // A Exits
//        window.setEnterTransition(fade); // B Enters
//        window.setReturnTransition(fade); // B Exits (Returns to A)
//        window.setReenterTransition(null); // A Enters (Returns from B)


//        window.setExitTransition(null);
//        window.setEnterTransition(null);
//        window.setReturnTransition(null);
//        window.setReenterTransition(null);


//        window.setExitTransition(slideBottomEdge);
//        window.setEnterTransition(slideTopEdge);
//        window.setReturnTransition(slideTopEdge);
//        window.setReenterTransition(slideBottomEdge);


//        window.setAllowEnterTransitionOverlap(false);
//        window.setAllowReturnTransitionOverlap(false);

    }

}
