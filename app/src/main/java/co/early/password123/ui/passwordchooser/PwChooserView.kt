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
import kotlinx.android.synthetic.main.fragment_pwchooser.view.aboutBackground
import kotlinx.android.synthetic.main.fragment_pwchooser.view.busyProgbar
import kotlinx.android.synthetic.main.fragment_pwchooser.view.cloudImg
import kotlinx.android.synthetic.main.fragment_pwchooser.view.eyeImg
import kotlinx.android.synthetic.main.fragment_pwchooser.view.infoImg
import kotlinx.android.synthetic.main.fragment_pwchooser.view.initialisingPercentbar
import kotlinx.android.synthetic.main.fragment_pwchooser.view.passwordEditText
import kotlinx.android.synthetic.main.fragment_pwchooser.view.pwchoose_debug_textview
import kotlinx.android.synthetic.main.fragment_pwchooser.view.pwchoose_poweredby_textview
import kotlinx.android.synthetic.main.fragment_pwchooser.view.pwchoose_warning_container
import kotlinx.android.synthetic.main.fragment_pwchooser.view.warningBackground
import kotlinx.android.synthetic.main.fragment_pwchooser.view.warningDetailText
import kotlinx.android.synthetic.main.fragment_pwchooser.view.warningText


class PwChooserView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0)
    : ScrollView(context, attrs, defStyleAttr, defStyleRes), SyncableView {


    //models that we need
    private lateinit var networkState: NetworkState
    private lateinit var pwned: Pwned
    private lateinit var passwordVisibility: PasswordVisibility
    private lateinit var logger: Logger


    private lateinit var animations: PwChooserAnimations


    //single observer reference
    internal var observer = this::syncView


    override fun onFinishInflate() {
        super.onFinishInflate()

        //supporting coloured progress bar for some older devices
        busyProgbar.indeterminateDrawable.setColorFilter(
            resources.getColor(R.color.colorAccent),
            android.graphics.PorterDuff.Mode.SRC_IN
        )

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
            cloudImg,
            warningBackground,
            infoImg,
            busyProgbar,
            initialisingPercentbar,
            warningText,
            warningDetailText,
            cloudImg, this, networkState, pwned,
            ViewUtils.getActivity(context)!!.currentActivityScreenWidthPx.toFloat(),
            ViewUtils.getActivity(context)!!.currentActivityScreenHeightPx.toFloat(),
            ViewUtils.getActivity(context)!!.densityScalingFactor
        )
    }

    private fun setClickListeners() {

        eyeImg.setOnClickListener { passwordVisibility.toggleVisibility() }
        passwordEditText.addTextChangedListener(TextWatcherCheckPassword(pwned))
        cloudImg.setOnClickListener { Toast.makeText(context, R.string.network_warning, Toast.LENGTH_SHORT).show() }
        pwchoose_poweredby_textview.setOnClickListener { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://haveibeenpwned.com/"))) }
        infoImg.setOnClickListener {
            animations.infoIconClicked(
                //(ViewGroup)PwChooserView.this.getParent() because we are a scrollview, the animation measurements
                //need a non scroll view parent to work robustly
                this@PwChooserView.parent as ViewGroup,
                infoImg,
                busyProgbar,
                passwordEditText,
                eyeImg,
                warningText,
                warningDetailText,
                cloudImg,
                aboutBackground,
                SuccessCallBack {
                    if (ViewUtils.getActivity(context)!!.hasWindowFocus()) {
                        AboutActivity.start(context)
                    }
                });
        }
    }

    //data binding stuff below - syncView() is a similar concept to the render() method in MVI

    override fun syncView() {

        animations.checkAll()

        val pwnedResult = pwned.pwnedResult

        val stateSet = intArrayOf(android.R.attr.state_checked * if (passwordVisibility.isVisible) 1 else -1)
        eyeImg.setImageState(stateSet, true)
        eyeImg.isEnabled = pwned.offlineDataLoadingComplete() && passwordEditText.text.length > 0
        passwordEditText.isEnabled = pwned.offlineDataLoadingComplete()
        passwordEditText.inputType = if (passwordVisibility.isVisible)
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        else
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        pwchoose_warning_container.visibility = if (pwnedResult.pwnedState == UNKNOWN) View.INVISIBLE else View.VISIBLE
        warningText.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult))
        warningText.text = formatWarningMessage(pwnedResult)
        warningDetailText.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult))
        warningDetailText.setText(if (pwnedResult.source == PwnedResult.Source.OFF_LINE) R.string.pwned_checked_offline else R.string.pwned_checked_online)
        pwchoose_debug_textview.visibility = if (pwnedResult.error != null) View.VISIBLE else View.GONE
        pwchoose_debug_textview.text = ResultFormatter.formatErrorMessage(pwnedResult.error)

        if (!animations.removeLoadingBarAnimRunning()) {//syncview will get called at the end of the animation anyway
            initialisingPercentbar.setProgressPercent(pwned.loadedPercent.toFloat())
            initialisingPercentbar.visibility = if (pwned.offlineDataLoadingComplete()) View.INVISIBLE else View.VISIBLE
        }
        if (!animations.busyTransitionAnimationsRunning()) {//syncview will get called at the end of the animation anyway
            busyProgbar.visibility = if (pwned.isBusy) View.VISIBLE else View.INVISIBLE
            infoImg.visibility = if (!pwned.isBusy && pwnedResult.pwnedState != UNKNOWN) View.VISIBLE else View.INVISIBLE
        }
        if (!animations.networkChangeAnimRunning()) {//syncview will get called at the end of the animation anyway
            cloudImg.visibility = if (!networkState.isConnected && pwned.offlineDataLoadingComplete()) View.VISIBLE else View.INVISIBLE
            warningBackground.visibility = if (networkState.isConnected) View.INVISIBLE else View.VISIBLE
        }
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        pwned.addObserver(observer)
        networkState.addObserver(observer)
        passwordVisibility.addObserver(observer)
        syncView() //  <- don't forget this
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pwned.removeObserver(observer)
        networkState.removeObserver(observer)
        passwordVisibility.removeObserver(observer)
    }
}
