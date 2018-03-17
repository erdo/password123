package co.early.password123.ui.common.anim;

import android.animation.TimeInterpolator;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.view.animation.Interpolator;


public class CustomEasing {

    public static final Interpolator flopUp = PathInterpolatorCompat.create(1.000f, -0.165f, 0.665f, 0.390f); //https://matthewlein.com/tools/ceaser
    public static final Interpolator innieOutie = PathInterpolatorCompat.create(0.475f, 0.075f, 0.345f, 0.880f);
    public static final TimeInterpolator bounceDown = new CordicBounceOut();

}
