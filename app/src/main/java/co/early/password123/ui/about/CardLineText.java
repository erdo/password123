package co.early.password123.ui.about;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.early.password123.R;


public class CardLineText extends LinearLayout {

    @BindView(R.id.aboutcardline_texticon_textview)
    protected TextView textIconView;

    @BindView(R.id.aboutcardline_text_textview)
    protected TextView textView;



    public CardLineText(Context context) {
        super(context);
    }

    public CardLineText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardLineText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);

    }


    public TextView getTextIconView(){
        return textIconView;
    }

    public TextView getTextView(){
        return textView;
    }

}
