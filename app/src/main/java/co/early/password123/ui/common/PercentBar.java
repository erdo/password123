package co.early.password123.ui.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import co.early.password123.R;


public class PercentBar extends AppCompatImageView {

    private Paint paint;

    private float widthPx;
    private float heightPx;

    private int backgroundColour;
    private int progressColour;

    private float percent = 0;


    public PercentBar(Context context) {
        super(context);
    }

    public PercentBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PercentBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        backgroundColour = getContext().getApplicationContext().getResources().getColor(R.color.colorPW123Transparent);
        progressColour = getContext().getApplicationContext().getResources().getColor(R.color.colorAccent);

        paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        widthPx = getWidth();
        heightPx = getHeight();

        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //background
        paint.setColor(backgroundColour);
        canvas.drawRect(0, 0, widthPx, heightPx, paint);

        //percent bar
        paint.setColor(progressColour);

        canvas.drawRect(0, 0, (percent * widthPx)/100, heightPx, paint);
    }


    public void setProgressPercent(float percent){

        this.percent = percent;

        invalidate();
    }

}
