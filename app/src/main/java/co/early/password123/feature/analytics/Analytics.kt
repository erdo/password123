package co.early.password123.feature.analytics

import co.early.asaf.core.Affirm
import co.early.asaf.core.logging.Logger
import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.CustomEvent


class Analytics(private val answers: Answers?, logger: Logger) {

    private val logger: Logger

    init {
        this.logger = Affirm.notNull(logger)
    }


    fun logEventRemotelyOrLocally(eventName: String) {

        Affirm.notNull(eventName)

        if (answers != null) {
            answers.logCustom(CustomEvent(eventName))
            logger.i(TAG_ONLINE, eventName)
        } else {
            logger.i(TAG_OFFLINE, eventName)
        }
    }

    companion object {

        private val TAG_ONLINE = "Analytics:ON"
        private val TAG_OFFLINE = "Analytics:OFF"
    }

}
