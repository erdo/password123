package co.early.password123;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import co.early.asaf.core.WorkMode;
import co.early.asaf.core.logging.AndroidLogger;
import co.early.asaf.core.logging.Logger;
import co.early.asaf.core.time.SystemTimeWrapper;
import co.early.password123.feature.networkmonitor.ActivityLifecycleNetworkCheck;
import co.early.password123.feature.networkmonitor.DoubleCheckConnection;
import co.early.password123.feature.networkmonitor.NetworkState;
import co.early.pwned.Pwned;
import co.early.pwned.feature.PwnedResult;

import static co.early.asaf.core.Affirm.notNull;
import static co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN;


class ObjectGraph {

    public static final String TAG = ObjectGraph.class.getSimpleName();

    private volatile boolean initialized = false;
    private final Map<Class<?>, Object> dependencies = new HashMap<>();


    void setApplication(Application application) {
        setApplication(application, WorkMode.ASYNCHRONOUS);
    }

    void setApplication(Application application, final WorkMode workMode) {

        notNull(application);
        notNull(workMode);


        // create dependency graph
        Pwned pwned = new Pwned(application);
        SystemTimeWrapper systemTimeWrapper = new SystemTimeWrapper();
        Logger logger = new AndroidLogger();
        DoubleCheckConnection doubleCheckConnection = new DoubleCheckConnection();
        NetworkState networkState = new NetworkState(
                (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE),
                application,
                doubleCheckConnection,
                workMode);
        application.registerActivityLifecycleCallbacks(new ActivityLifecycleNetworkCheck(networkState));


        // add models to the dependencies map if you will need them later
        dependencies.put(Pwned.class, pwned);
        dependencies.put(SystemTimeWrapper.class, systemTimeWrapper);
        dependencies.put(NetworkState.class, networkState);
        dependencies.put(Logger.class, logger);

    }

    void init() {
        if (!initialized) {
            initialized = true;

            // run any necessary initialization code once object graph has been created here

            Pwned pwned = get(Pwned.class);
            NetworkState networkState = get(NetworkState.class);
            Logger logger = get(Logger.class);

            //load offline pwd db - delayed because we want the keyboard to finish popping up first
            new Handler().postDelayed(() -> pwned.loadPwdDb(() -> logger.i(TAG, "Offline pwd db loaded"),
                    failureMessage -> logger.e(TAG, "failed to load pwd db:" + failureMessage)), 1500);

            // enable network monitor
            networkState.enable();

            // setup observer so that when network returns, pwned retries search automatically
            networkState.addObserver(() -> {
                if (networkState.isConnected()) {
                    pwned.retryWithNetwork();
                }
            });

            pwned.addObserver(() -> {
                // setup observer so that if we ever get a successful network call back, let the networkState model know
                if (pwned.getPwnedResult().source == PwnedResult.Source.ON_LINE && pwned.getPwnedResult().pwnedState != UNKNOWN){
                    networkState.checkIfDown();
                // setup observer so that if we ever get a failed network call back, let the networkState model know
                } else if (pwned.getPwnedResult().pwnedState == UNKNOWN && pwned.getPwnedResult().source == PwnedResult.Source.ON_LINE){
                    networkState.checkIfUp();
                }
            });

        }
    }

    <T> T get(Class<T> model) {

        notNull(model);
        T t = model.cast(dependencies.get(model));
        notNull(t);

        return t;
    }

    <T> void putMock(Class<T> clazz, T object) {

        notNull(clazz);
        notNull(object);

        dependencies.put(clazz, object);
    }

}
