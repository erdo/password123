package co.early.password123.ui.common.anim;

import android.animation.TimeInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;


public class CustomEasing {

    //https://matthewlein.com/tools/ceaser

    public static Interpolator flopUp = PathInterpolatorCompat.create(0.865f, -0.515f, 0.925f, 0.615f);
    public static TimeInterpolator bounceDown = new CordicBounceOut();

}
