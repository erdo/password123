package co.early.password123.ui.passwordchooser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.early.asaf.core.callbacks.SuccessCallBack;
import co.early.asaf.core.logging.Logger;
import co.early.asaf.core.observer.Observer;
import co.early.asaf.core.ui.SyncableView;
import co.early.password123.CustomApp;
import co.early.password123.R;
import co.early.password123.feature.networkmonitor.NetworkState;
import co.early.password123.feature.passwordvisibility.PasswordVisibility;
import co.early.password123.ui.about.AboutActivity;
import co.early.password123.ui.common.TextWatcherCheckPassword;
import co.early.password123.ui.common.ViewUtils;
import co.early.password123.ui.common.widgets.LockedKeyboardEditText;
import co.early.password123.ui.common.widgets.PercentBar;
import co.early.pwned.Pwned;
import co.early.pwned.feature.PwnedResult;

import static co.early.password123.ui.passwordchooser.ResultFormatter.formatWarningMessage;
import static co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN;


public class PwChooserView extends ScrollView implements SyncableView {

    //models that we need to sync with
    private NetworkState networkState;
    private Pwned pwned;
    private PasswordVisibility passwordVisibility;
    private Logger logger;


    @BindView(R.id.pwchoose_cloud_img)
    protected ImageView cloudIcon;

    @BindView(R.id.pwchoose_transitionbackground_view)
    protected View transitionBackground;

    @BindView(R.id.pwchoose_warningbackground_view)
    protected View backgroundWarn;

    @BindView(R.id.pwchoose_password_edittext)
    protected LockedKeyboardEditText password;

    @BindView(R.id.pwchoose_eye_img)
    protected ImageView eyeView;

    @BindView(R.id.pwchoose_initbar_animatedprogbar)
    protected PercentBar initProgress;

    @BindView(R.id.pwchoose_warning_container)
    protected View warningContainer;

    @BindView(R.id.pwchoose_warning_textview)
    protected TextView warningText;

    @BindView(R.id.pwchoose_warningdetail_textview)
    protected TextView warningDetail;

    @BindView(R.id.pwchoose_debug_textview)
    protected TextView debugText;

    @BindView(R.id.pwchoose_info_img)
    protected ImageView infoIcon;

    @BindView(R.id.pwchoose_busy_progressbar)
    protected ProgressBar busy;

    @BindView(R.id.pwchoose_poweredby_textview)
    protected TextView poweredBy;



    private PwChooserAnimations animations;


    //single observer reference
    Observer observer = this::syncView;


    public PwChooserView(Context context) {
        super(context);
    }

    public PwChooserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PwChooserView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);

        //supporting coloured progress bar for some older devices
        busy.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorAccent),
                android.graphics.PorterDuff.Mode.SRC_IN);

        getModelReferences();

        setupAnimationTriggers();

        setClickListeners();
    }

    private void getModelReferences() {
        networkState = CustomApp.get(NetworkState.class);
        pwned = CustomApp.get(Pwned.class);
        passwordVisibility = CustomApp.get(PasswordVisibility.class);
        logger = CustomApp.get(Logger.class);
    }

    private void setupAnimationTriggers() {
        animations = new PwChooserAnimations(cloudIcon, backgroundWarn, infoIcon, busy, initProgress,
                warningText, warningDetail, cloudIcon, this, networkState, pwned,
                ViewUtils.getActivity(getContext()).getCurrentActivityScreenWidthPx(),
                ViewUtils.getActivity(getContext()).getCurrentActivityScreenHeightPx(),
                ViewUtils.getActivity(getContext()).getDensityScalingFactor());
    }

    private void setClickListeners() {

        eyeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordVisibility.toggleVisibility();
            }
        });
        password.addTextChangedListener(new TextWatcherCheckPassword(pwned));
        cloudIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.network_warning, Toast.LENGTH_SHORT).show();
            }
        });
        poweredBy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://haveibeenpwned.com/")));
            }
        });
        infoIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //(ViewGroup)PwChooserView.this.getParent() because we are a scrollview, the animation measurements
                //need a non scroll view parent to work robustly
                animations.infoIconClicked((ViewGroup)PwChooserView.this.getParent(), infoIcon, busy, password, eyeView,
                        warningText, warningDetail, cloudIcon, transitionBackground, new SuccessCallBack() {
                    @Override
                    public void success() {
                        if (ViewUtils.getActivity(getContext()).hasWindowFocus()){
                            AboutActivity.start(getContext());
                        }
                    }
                });
            }
        });
    }


    //data binding stuff below

    public void syncView() {

        animations.checkAll();

        PwnedResult pwnedResult = pwned.getPwnedResult();

        final int[] stateSet = {android.R.attr.state_checked * (passwordVisibility.isVisible() ? 1 : -1)};
        eyeView.setImageState(stateSet, true);
        eyeView.setEnabled(pwned.offlineDataLoadingComplete());
        password.setEnabled(pwned.offlineDataLoadingComplete());
        password.setInputType(passwordVisibility.isVisible() ?
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        password.setSelection(password.getText().length());
        warningContainer.setVisibility(pwnedResult.pwnedState == UNKNOWN ? INVISIBLE : VISIBLE);
        warningText.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult));
        warningText.setText(formatWarningMessage(pwnedResult));
        warningDetail.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult));
        warningDetail.setText(pwnedResult.source == PwnedResult.Source.OFF_LINE ? R.string.pwned_checked_offline : R.string.pwned_checked_online);
        debugText.setVisibility(pwnedResult.error != null ? VISIBLE : GONE);
        debugText.setText(ResultFormatter.formatErrorMessage(pwnedResult.error));

        if (!animations.removeLoadingBarAnimRunning()) {//syncview will get called at the end of the animation anyway
            initProgress.setProgressPercent(pwned.getLoadedPercent());
            initProgress.setVisibility(pwned.offlineDataLoadingComplete() ? INVISIBLE : VISIBLE);
        }
        if (!animations.busyTransitionAnimationsRunning()) {//syncview will get called at the end of the animation anyway
            busy.setVisibility(pwned.isBusy() ? VISIBLE : INVISIBLE);
            infoIcon.setVisibility(!pwned.isBusy() && pwnedResult.pwnedState != UNKNOWN ? VISIBLE : INVISIBLE);
        }
        if (!animations.networkChangeAnimRunning()) {//syncview will get called at the end of the animation anyway
            cloudIcon.setVisibility(!networkState.isConnected() && pwned.offlineDataLoadingComplete() ? VISIBLE : INVISIBLE);
            backgroundWarn.setVisibility(networkState.isConnected() ? INVISIBLE : VISIBLE);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pwned.addObserver(observer);
        networkState.addObserver(observer);
        passwordVisibility.addObserver(observer);
        syncView(); //  <- don't forget this
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pwned.removeObserver(observer);
        networkState.removeObserver(observer);
        passwordVisibility.removeObserver(observer);
    }
}
