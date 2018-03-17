package co.early.password123.feature.analytics;

import android.support.annotation.Nullable;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;

import co.early.asaf.core.Affirm;
import co.early.asaf.core.logging.Logger;


public class Analytics {

    private static final String TAG_ONLINE = "Antics:ONLINE";
    private static final String TAG_OFFLINE = "Antics:OFFLINE";

    @Nullable
    private final Answers answers;
    private final Logger logger;

    public Analytics(@Nullable Answers answers, Logger logger) {
        this.answers = answers;
        this.logger = Affirm.notNull(logger);
    }


    public void logEventRemotelyOrLocally(String evetName){

        Affirm.notNull(evetName);

        if (answers != null){
            answers.logCustom(new CustomEvent(evetName));
            logger.i(TAG_ONLINE, evetName);
        }else{
            logger.i(TAG_OFFLINE, evetName);
        }
    }

}
