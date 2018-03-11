package co.early.password123.ui.passwordchooser;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.early.asaf.core.logging.Logger;
import co.early.asaf.core.observer.Observer;
import co.early.asaf.core.ui.SyncableView;
import co.early.password123.CustomApp;
import co.early.password123.R;
import co.early.password123.feature.networkmonitor.NetworkState;
import co.early.password123.ui.about.AboutActivity;
import co.early.password123.ui.common.PercentBar;
import co.early.password123.ui.common.TextWatcherCheckPassword;
import co.early.pwned.Pwned;
import co.early.pwned.feature.PwnedResult;

import static co.early.password123.ui.passwordchooser.ResultFormatter.formatWarningMessage;
import static co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN;


public class PwChooserView extends ScrollView implements SyncableView{

    //models that we need to sync with
    private NetworkState networkState;
    private Pwned pwned;
    private Logger logger;


    @BindView(R.id.pwchoose_cloud_img)
    protected ImageView networkIcon;

    @BindView(R.id.pwchoose_warningbackground_view)
    protected View backgroundWarn;

    @BindView(R.id.pwchoose_password_edittext)
    protected EditText password;

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

        setListeners();
    }

    private void getModelReferences(){
        networkState = CustomApp.get(NetworkState.class);
        pwned = CustomApp.get(Pwned.class);
        logger = CustomApp.get(Logger.class);
    }

    private void setupAnimationTriggers(){

        animations = new PwChooserAnimations(networkIcon, backgroundWarn, infoIcon, busy, initProgress,
                this, networkState, pwned);
    }

    private void setListeners(){
        password.addTextChangedListener(new TextWatcherCheckPassword(password, pwned));
        networkIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.network_warning, Toast.LENGTH_SHORT).show();
            }
        });
        infoIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.start(getContext());
                //AboutActivity.startWithTransition(ViewUtils.getActivityFromContext(getContext()), Pair.create(infoIcon, "about_info_img"));
            }
        });
    }


    //data binding stuff below

    public void syncView(){

        animations.checkAllLazy();

        PwnedResult pwnedResult = pwned.getPwnedResult();

        password.setEnabled(pwned.offlineDataLoadingComplete());
        warningContainer.setVisibility(pwnedResult.pwnedState == UNKNOWN ? INVISIBLE : VISIBLE);
        warningText.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult));
        warningText.setText(formatWarningMessage(pwnedResult));
        warningDetail.setTextColor(ResultFormatter.getResovledColourForSeverity(pwnedResult));
        warningDetail.setText(pwnedResult.source == PwnedResult.Source.OFF_LINE ? R.string.pwned_checked_offline : R.string.pwned_checked_online);
        debugText.setVisibility(pwnedResult.error != null ?  VISIBLE : GONE);
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
            networkIcon.setVisibility(!networkState.isConnected() && pwned.offlineDataLoadingComplete() ? VISIBLE : INVISIBLE);
            backgroundWarn.setVisibility(networkState.isConnected() ? INVISIBLE : VISIBLE);
        }
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        pwned.addObserver(observer);
        networkState.addObserver(observer);
        syncView(); //  <- don't forget this
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pwned.removeObserver(observer);
        networkState.removeObserver(observer);
    }
}
