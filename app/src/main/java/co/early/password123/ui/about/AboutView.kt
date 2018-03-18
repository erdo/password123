package co.early.password123.ui.about

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.widget.ScrollView
import co.early.asaf.core.logging.Logger
import co.early.password123.CustomApp
import co.early.password123.R
import co.early.password123.feature.analytics.Analytics
import co.early.password123.feature.analytics.AnalyticsConstants.Events.InfoScreen.Companion.SEARCH_FOR_PASSWORD_MGR
import co.early.password123.ui.common.widgets.CardLineImage
import co.early.password123.ui.common.widgets.CardLineText
import kotlinx.android.synthetic.main.fragment_about.view.*


class AboutView : ScrollView {

    //models that we need to sync with
    private var logger: Logger? = null
    private var analytics: Analytics? = null



    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onFinishInflate() {
        super.onFinishInflate()

        getModelReferences()

        setupContent()

        setListeners()
    }

    private fun getModelReferences() {
        logger = CustomApp.get(Logger::class.java)
        analytics = CustomApp.get(Analytics::class.java)
    }

    private fun setupContent() {

        (about_tooshort_cardlinetxt as CardLineText).textIconView!!.setTextColor(CustomApp.inst!!.getResources().getColor(R.color.colorPW123Severity3))
        (about_tooshort_cardlinetxt as CardLineText).textIconView!!.setText(R.string.pwned_state_tooshort)
        (about_tooshort_cardlinetxt as CardLineText).textView!!.setText(R.string.pwned_detail_tooshort)

        (about_pwned_cardlinetxt as CardLineText).textIconView!!.setTextColor(CustomApp.inst!!.getResources().getColor(R.color.colorPW123Severity3))
        (about_pwned_cardlinetxt as CardLineText).textIconView!!.setText(R.string.pwned_state_pwned)
        (about_pwned_cardlinetxt as CardLineText).textView!!.setText(R.string.pwned_detail_pwned)

        (about_unknown_cardlinetxt as CardLineText).textIconView!!.setTextColor(CustomApp.inst!!.getResources().getColor(R.color.colorAccent))
        (about_unknown_cardlinetxt as CardLineText).textIconView!!.setText(R.string.pwned_state_unknown)
        (about_unknown_cardlinetxt as CardLineText).textView!!.setText(R.string.pwned_detail_unknown)

        (about_potentiallyok_cardlinetxt as CardLineText).textIconView!!.setTextColor(CustomApp.inst!!.getResources().getColor(R.color.colorPW123Severity0))
        (about_potentiallyok_cardlinetxt as CardLineText).textIconView!!.setText(R.string.pwned_state_ok)
        (about_potentiallyok_cardlinetxt as CardLineText).textView!!.setText(R.string.pwned_detail_ok)

        (about_pmanager_cardlineimg as CardLineImage).textView!!.setText(R.string.password_manager)
        (about_pmanager_cardlineimg as CardLineImage).imageIconView!!.setImageResource(R.drawable.passwordmgr_black)

        (about_tech_cardlineimg as CardLineImage).textView!!.setText(R.string.about_tech)
        (about_tech_cardlineimg as CardLineImage).imageIconView!!.setImageResource(R.drawable.technology_black)

        (about_licence_cardlineimg as CardLineImage).textView!!.setText(R.string.licences)
        (about_licence_cardlineimg as CardLineImage).imageIconView!!.setImageResource(R.drawable.legal_black)

    }


    private fun setListeners() {
        (about_pmanager_card as CardView).setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://duckduckgo.com/?q=password+managers")))
            analytics!!.logEventRemotelyOrLocally(SEARCH_FOR_PASSWORD_MGR)
        }
    }

}
