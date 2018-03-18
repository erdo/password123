package co.early.password123.ui.common.widgets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.common_cardline_text.view.*


class CardLineText : LinearLayout {


    var textIconView: TextView? = null
    var textView: TextView? = null


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onFinishInflate() {
        super.onFinishInflate()

        textView = aboutcardline_text_textview
        textIconView = aboutcardline_texticon_textview

    }

}
