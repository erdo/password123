package co.early.password123.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ScrollView;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.early.asaf.core.logging.Logger;
import co.early.password123.CustomApp;
import co.early.password123.R;
import co.early.password123.feature.analytics.Analytics;
import co.early.password123.ui.common.widgets.CardLineImage;
import co.early.password123.ui.common.widgets.CardLineText;

import static co.early.password123.feature.analytics.AnalyticsConstants.Events.InfoScreen.SEARCH_FOR_PASSWORD_MGR;


public class AboutView extends ScrollView{

    //models that we need to sync with
    private Logger logger;
    private Analytics analytics;


    @BindView(R.id.about_info_img)
    protected ImageView infoIcon;



    @BindView(R.id.about_tooshort_cardlinetxt)
    protected CardLineText tooShortLine;

    @BindView(R.id.about_pwned_cardlinetxt)
    protected CardLineText pwnedLine;

    @BindView(R.id.about_unknown_cardlinetxt)
    protected CardLineText unknownLine;

    @BindView(R.id.about_potentiallyok_cardlinetxt)
    protected CardLineText okLine;



    @BindView(R.id.about_pmanager_card)
    protected CardView cardPasswordManager;

    @BindView(R.id.about_pmanager_cardlineimg)
    protected CardLineImage cardPManagerLineImage;



    @BindView(R.id.about_tech_cardlineimg)
    protected CardLineImage techLine;



    @BindView(R.id.about_licence_cardlineimg)
    protected CardLineImage licencesLine;




    public AboutView(Context context) {
        super(context);
    }

    public AboutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AboutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        ButterKnife.bind(this, this);

        getModelReferences();

        setupContent();

        setListeners();
    }

    private void getModelReferences(){
        logger = CustomApp.get(Logger.class);
        analytics = CustomApp.get(Analytics.class);
    }

    private void setupContent(){

        tooShortLine.getTextIconView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorPW123Severity3));
        tooShortLine.getTextIconView().setText(R.string.pwned_state_tooshort);
        tooShortLine.getTextView().setText(R.string.pwned_detail_tooshort);

        pwnedLine.getTextIconView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorPW123Severity3));
        pwnedLine.getTextIconView().setText(R.string.pwned_state_pwned);
        pwnedLine.getTextView().setText(R.string.pwned_detail_pwned);

        unknownLine.getTextIconView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        unknownLine.getTextIconView().setText(R.string.pwned_state_unknown);
        unknownLine.getTextView().setText(R.string.pwned_detail_unknown);

        okLine.getTextIconView().setTextColor(ContextCompat.getColor(getContext(), R.color.colorPW123Severity0));
        okLine.getTextIconView().setText(R.string.pwned_state_ok);
        okLine.getTextView().setText(R.string.pwned_detail_ok);

        cardPManagerLineImage.getTextView().setText(R.string.password_manager);
        cardPManagerLineImage.getImageIconView().setImageResource(R.drawable.passwordmgr_black);

        techLine.getTextView().setText(R.string.about_tech);
        techLine.getImageIconView().setImageResource(R.drawable.technology_black);

        licencesLine.getTextView().setText(R.string.licences);
        licencesLine.getImageIconView().setImageResource(R.drawable.legal_black);

    }


    private void setListeners(){
        cardPasswordManager.setOnClickListener(v -> {
            getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://duckduckgo.com/?q=password+managers")));
            analytics.logEventRemotelyOrLocally(SEARCH_FOR_PASSWORD_MGR);
        });
    }

}
