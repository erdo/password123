package co.early.password123.ui.common.anim

import android.animation.TimeInterpolator


abstract class Cordic : TimeInterpolator {

    internal abstract val normalised1000CordicTable: IntArray

    override fun getInterpolation(input: Float): Float {

        if (cordicTable == null) {
            cordicTable = normalised1000CordicTable
        }
        return 1f - cordicTable!![(input * 1000).toInt()].toFloat() / 1000
    }

    companion object {

        // table data normalized to 1000
        private var cordicTable: IntArray? = null
    }

}
