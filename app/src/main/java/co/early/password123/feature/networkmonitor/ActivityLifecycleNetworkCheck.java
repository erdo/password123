package co.early.password123.feature.networkmonitor;


import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import co.early.asaf.core.Affirm;


public class ActivityLifecycleNetworkCheck implements ActivityLifecycleCallbacks {

    private final NetworkState networkState;

    public ActivityLifecycleNetworkCheck(NetworkState networkState) {
        this.networkState = Affirm.notNull(networkState);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        networkState.checkIfDown();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
