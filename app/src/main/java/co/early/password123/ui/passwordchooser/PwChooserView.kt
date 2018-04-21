package co.early.password123.ui.passwordchooser

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.InputType
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import co.early.asaf.core.callbacks.SuccessCallBack
import co.early.asaf.core.logging.Logger
import co.early.asaf.core.observer.Observer
import co.early.asaf.core.ui.SyncableView
import co.early.password123.CustomApp
import co.early.password123.R
import co.early.password123.feature.networkmonitor.NetworkState
import co.early.password123.feature.passwordvisibility.PasswordVisibility
import co.early.password123.ui.about.AboutActivity
import co.early.password123.ui.common.ViewUtils
import co.early.password123.ui.passwordchooser.ResultFormatter.formatWarningMessage
import co.early.pwned.Pwned
import co.early.pwned.feature.PwnedResult
import co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN
import kotlinx.android.synthetic.main.fragment_pwchooser.view.*


class PwChooserView : ScrollView, SyncableView {

    //models that we need to sync with
    private var networkState: NetworkState? = null
    private var pwned: Pwned? = null
    private var passwordVisibility: PasswordVisibility? = null
    private var logger: Logger? = null


    private var animations: PwChooserAnimations? = null


    //single observer reference
    internal var observer = Observer { this.syncView() }


    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}


    override fun onFinishInflate() {
        super.onFinishInflate()

        //supporting coloured progress bar for some older devices
        pwchoose_busy_progressbar.indeterminateDrawable.setColorFilter(
                resources.getColor(R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_IN)

        getModelReferences()

        setupAnimationTriggers()

        setClickListeners()
    }

    private fun getModelReferences() {
        networkState = CustomApp.get(NetworkState::class.java)
        pwned = CustomApp.get(Pwned::class.java)
        passwordVisibility = CustomApp.get(PasswordVisibility::class.java)
        logger = CustomApp.get(Logger::class.java)
    }

    private fun setupAnimationTriggers() {
        animations = PwChooserAnimations(
                pwchoose_cloud_img,
                pwchoose_warningbackground_view,
                pwchoose_info_img,
                pwchoose_busy_progressbar,
                pwchoose_initbar_animatedprogbar,
                pwchoose_warning_textview,
                pwchoose_warningdetail_textview,
                pwchoose_cloud_img, this, networkState!!, pwned!!,
                ViewUtils.getActivity(context)!!.currentActivityScreenWidthPx.toFloat(),
                ViewUtils.getActivity(context)!!.currentActivityScreenHeightPx.toFloat(),
                ViewUtils.getActivity(context)!!.densityScalingFactor)
    }

    private fun setClickListeners() {

        pwchoose_eye_img.setOnClickListener { passwordVisibility!!.toggleVisibility() }
        pwchoose_password_edittext.addTextChangedListener(TextWatcherCheckPassword(pwned!!))
        pwchoose_cloud_img.setOnClickListener { Toast.makeText(context, R.string.network_warning, Toast.LENGTH_SHORT).show() }
        pwchoose_poweredby_textview.setOnClickListener { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://haveibeenpwned.com/"))) }
        pwchoose_info_img.setOnClickListener {
            animations!!.infoIconClicked(
                    //(ViewGroup)PwChooserView.this.getParent() because we are a scrollview, the animation measurements
                    //need a non scroll view parent to work robustly
                    this@PwChooserView.parent as ViewGroup,
                    pwchoose_info_img,
                    pwchoose_busy_progressbar,
                    pwchoose_password_edittext,
                    pwchoose_eye_img,
                    pwchoose_warning_textview,
                    pwchoose_warningdetail_textview,
                    pwchoose_cloud_img,
                    pwchoose_transitionbackground_view,
                    SuccessCallBack {
                        if (ViewUtils.getActivity(context)!!.hasWindowFocus()) {
                            AboutActivity.start(context)
                        }
                    });
        }
    }

    //data binding stuff below

    override fun syncView() {

        animations!!.checkAll()

        val pwnedResult = pwned!!.pwnedResult

        val stateSet = intArrayOf(android.R.attr.state_checked * if (passwordVisibility!!.isVisible) 1 else -1)
        pwchoose_eye_img.setImageState(stateSet, true)
        pwchoose_eye_img.isEnabled = pwned!!.offlineDataLoadingComplete() && pwchoose_password_edittext.text.length > 0
        pwchoose_password_edittext.isEnabled = pwned!!.offlineDataLoadingComplete()
        pwchoose_password_edittext.inputType = if (passwordVisibility!!.isVisible)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pwchoose_warning_container.visibility = if (pwnedResult.pwnedState == UNKNOWN) View.INVISIBLE else View.VISIBLE
        pwchoose_warning_textview.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult))
        pwchoose_warning_textview.text = formatWarningMessage(pwnedResult)
        pwchoose_warningdetail_textview.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult))
        pwchoose_warningdetail_textview.setText(if (pwnedResult.source == PwnedResult.Source.OFF_LINE) R.string.pwned_checked_offline else R.string.pwned_checked_online)
        pwchoose_debug_textview.visibility = if (pwnedResult.error != null) View.VISIBLE else View.GONE
        pwchoose_debug_textview.text = ResultFormatter.formatErrorMessage(pwnedResult.error)

        if (!animations!!.removeLoadingBarAnimRunning()) {//syncview will get called at the end of the animation anyway
            pwchoose_initbar_animatedprogbar.setProgressPercent(pwned!!.loadedPercent.toFloat())
            pwchoose_initbar_animatedprogbar.visibility = if (pwned!!.offlineDataLoadingComplete()) View.INVISIBLE else View.VISIBLE
        }
        if (!animations!!.busyTransitionAnimationsRunning()) {//syncview will get called at the end of the animation anyway
            pwchoose_busy_progressbar.visibility = if (pwned!!.isBusy) View.VISIBLE else View.INVISIBLE
            pwchoose_info_img.visibility = if (!pwned!!.isBusy && pwnedResult.pwnedState != UNKNOWN) View.VISIBLE else View.INVISIBLE
        }
        if (!animations!!.networkChangeAnimRunning()) {//syncview will get called at the end of the animation anyway
            pwchoose_cloud_img.visibility = if (!networkState!!.isConnected && pwned!!.offlineDataLoadingComplete()) View.VISIBLE else View.INVISIBLE
            pwchoose_warningbackground_view.visibility = if (networkState!!.isConnected) View.INVISIBLE else View.VISIBLE
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        pwned!!.addObserver(observer)
        networkState!!.addObserver(observer)
        passwordVisibility!!.addObserver(observer)
        syncView() //  <- don't forget this
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pwned!!.removeObserver(observer)
        networkState!!.removeObserver(observer)
        passwordVisibility!!.removeObserver(observer)
    }
}
