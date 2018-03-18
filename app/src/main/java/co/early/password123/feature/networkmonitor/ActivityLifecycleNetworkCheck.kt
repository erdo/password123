package co.early.password123.feature.networkmonitor


import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle

import co.early.asaf.core.Affirm


class ActivityLifecycleNetworkCheck(networkState: NetworkState) : ActivityLifecycleCallbacks {

    private val networkState: NetworkState

    init {
        this.networkState = Affirm.notNull(networkState)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        networkState.checkIfDown()
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}
