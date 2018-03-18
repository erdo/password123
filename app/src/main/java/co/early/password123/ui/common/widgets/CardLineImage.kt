package co.early.password123.ui.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.common_cardline_img.view.*


class CardLineImage : LinearLayout {

    var imageIconView: ImageView? = null
    var textView: TextView? = null


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onFinishInflate() {
        super.onFinishInflate()

        imageIconView = aboutcardline_imgicon_imgview
        textView = aboutcardline_text_textview

    }

}
