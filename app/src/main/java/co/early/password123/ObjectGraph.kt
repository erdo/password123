package co.early.password123

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import co.early.asaf.core.Affirm.notNull
import co.early.asaf.core.WorkMode
import co.early.asaf.core.logging.AndroidLogger
import co.early.asaf.core.logging.Logger
import co.early.asaf.core.time.SystemTimeWrapper
import co.early.password123.feature.analytics.Analytics
import co.early.password123.feature.networkmonitor.ActivityLifecycleNetworkCheck
import co.early.password123.feature.networkmonitor.DoubleCheckConnection
import co.early.password123.feature.networkmonitor.NetworkState
import co.early.password123.feature.passwordvisibility.PasswordVisibility
import co.early.pwned.Pwned
import co.early.pwned.feature.PwnedResult
import co.early.pwned.feature.PwnedResult.IsPwned.UNKNOWN
import co.early.pwned.feature.PwnedRouter
import co.early.pwned.feature.offline.OfflinePwdDb
import com.crashlytics.android.answers.Answers
import java.util.*


internal class ObjectGraph {

    @Volatile private var initialized = false
    private val dependencies = HashMap<Class<*>, Any>()

    @JvmOverloads
    fun setApplication(application: Application, workMode: WorkMode = WorkMode.ASYNCHRONOUS) {

        notNull(application)
        notNull(workMode)


        // create dependency graph
        val logger = AndroidLogger("P123_")
        //Pwned pwned = new Pwned(application);
        val pwned = Pwned(application, OfflinePwdDb.SIZE_10000, PwnedRouter.OnlineMethod.K_ANON, WorkMode.ASYNCHRONOUS, logger)
        val systemTimeWrapper = SystemTimeWrapper()
        val doubleCheckConnection = DoubleCheckConnection()
        val networkState = NetworkState(
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager,
                application,
                doubleCheckConnection,
                logger,
                workMode)
        application.registerActivityLifecycleCallbacks(ActivityLifecycleNetworkCheck(networkState))
        val analytics = Analytics(if (BuildConfig.DEBUG) null else Answers.getInstance(), logger)
        val passwordVisibility = PasswordVisibility(workMode)

        // add models to the dependencies map if you will need them later
        dependencies.put(Pwned::class.java, pwned)
        dependencies.put(SystemTimeWrapper::class.java, systemTimeWrapper)
        dependencies.put(NetworkState::class.java, networkState)
        dependencies.put(Analytics::class.java, analytics)
        dependencies.put(PasswordVisibility::class.java, passwordVisibility)
        dependencies.put(Logger::class.java, logger)

    }

    fun init() {
        if (!initialized) {
            initialized = true

            // run any necessary initialization code once object graph has been created here

            val pwned = get(Pwned::class.java)
            val networkState = get(NetworkState::class.java)
            val logger = get(Logger::class.java)

            //load offline pwd db - delayed because we want the keyboard to finish popping up first
            pwned.loadPwdDb(
                    { logger.i(TAG, "Offline pwd db loaded") },
                    { failureMessage -> logger.e(TAG, "failed to load pwd db:" + failureMessage) }
            );

            // enable network monitor
            networkState.enable()

            // setup observer so that when network returns, pwned retries search automatically
            networkState.addObserver {
                if (networkState.isConnected) {
                    pwned.retryWithNetwork()
                }
            }

            pwned.addObserver {
                // setup observer so that if we ever get a successful network call back, let the networkState model know
                if (pwned.pwnedResult.source == PwnedResult.Source.ON_LINE && pwned.pwnedResult.pwnedState != UNKNOWN) {
                    networkState.checkIfDown()
                    // setup observer so that if we ever get a failed network call back, let the networkState model know
                } else if (pwned.pwnedResult.pwnedState == UNKNOWN && pwned.pwnedResult.source == PwnedResult.Source.ON_LINE) {
                    networkState.checkIfUp()
                }
            }

        }
    }

    operator fun <T> get(model: Class<T>): T {

        notNull(model)
        val t = model.cast(dependencies[model])
        notNull(t)

        return t
    }

    fun <T> putMock(clazz: Class<T>, `object`: T) {

        notNull(clazz)
        notNull(`object`)

        dependencies.put(clazz, `object`!!)
    }

    companion object {
        val TAG = ObjectGraph::class.java.simpleName
    }

}
