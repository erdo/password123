package co.early.password123.ui.common.anim;

import android.animation.TimeInterpolator;


public abstract class Cordic implements TimeInterpolator {

    // table data normalized to 1000
    private static int[] cordicTable;

    @Override
    public float getInterpolation(float input) {

        if (cordicTable == null){
            cordicTable = getNormalised1000CordicTable();
        }
        return 1f - (float) cordicTable[(int) (input * 1000)]/1000;
    }

    abstract int[] getNormalised1000CordicTable();

}
