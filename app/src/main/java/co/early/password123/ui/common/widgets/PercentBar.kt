package co.early.password123.ui.common.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

import co.early.password123.R


class PercentBar : AppCompatImageView {

    private var paint: Paint? = null

    private var widthPx: Float = 0.toFloat()
    private var heightPx: Float = 0.toFloat()

    private var backgroundColour: Int = 0
    private var progressColour: Int = 0

    private var percent = 0f


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    public override fun onFinishInflate() {
        super.onFinishInflate()

        backgroundColour = ContextCompat.getColor(context,R.color.colorPW123Transparent)
        progressColour = ContextCompat.getColor(context,R.color.colorAccent)

        paint = Paint()
        paint!!.flags = Paint.ANTI_ALIAS_FLAG
        paint!!.style = Paint.Style.FILL_AND_STROKE
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        widthPx = width.toFloat()
        heightPx = height.toFloat()

        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //background
        paint!!.color = backgroundColour
        canvas.drawRect(0f, 0f, widthPx, heightPx, paint!!)

        //percent bar
        paint!!.color = progressColour

        canvas.drawRect(0f, 0f, percent * widthPx / 100, heightPx, paint!!)
    }


    fun setProgressPercent(percent: Float) {

        this.percent = percent

        invalidate()
    }

}
