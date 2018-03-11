package co.early.password123.ui.about;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.early.password123.R;


public class CardLineImage extends LinearLayout {

    @BindView(R.id.aboutcardline_imgicon_imgview)
    protected ImageView imageIconView;

    @BindView(R.id.aboutcardline_text_textview)
    protected TextView textView;



    public CardLineImage(Context context) {
        super(context);
    }

    public CardLineImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardLineImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);

    }


    public ImageView getImageIconView(){
        return imageIconView;
    }

    public TextView getTextView(){
        return textView;
    }

}
