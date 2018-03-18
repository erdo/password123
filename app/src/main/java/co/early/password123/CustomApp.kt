package co.early.password123

import android.app.Application
import co.early.asaf.core.Affirm
import co.early.asaf.core.WorkMode
import com.crashlytics.android.Crashlytics
import com.crashlytics.android.answers.Answers
import com.parse.Parse
import io.fabric.sdk.android.Fabric

/**
 * Try not to fill this class with lots of code, if possible move it to a model somewhere
 */
class CustomApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //third party library setup
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, Crashlytics(), Answers())
        }
        Parse.initialize(this)


        inst = this

        if (objectGraph == null) {
            objectGraph = ObjectGraph()
        }
        objectGraph!!.setApplication(this)

    }

    fun injectSynchronousObjectGraph() {
        objectGraph = ObjectGraph()
        objectGraph!!.setApplication(this, WorkMode.SYNCHRONOUS)
    }

    fun <T> injectMockObject(clazz: Class<T>, `object`: T) {
        objectGraph!!.putMock(clazz, `object`)
    }

    companion object {

        var inst: CustomApp? = null
            private set
        private var objectGraph: ObjectGraph? = null

        // unfortunately the android test runner calls Application.onCreate() once _before_ we get a
        // chance to call createApplication() in ApplicationTestCase (contrary to the documentation).
        // So to prevent initialisation stuff happening before we have had a chance to set our mocks
        // during tests, we need to separate out the loadPwdDb() stuff, which is why we put it here,
        // to be called by the base activity of the app
        // http://stackoverflow.com/questions/4969553/how-to-prevent-activityunittestcase-from-calling-application-oncreate
        fun init() {
            if (objectGraph != null) {
                objectGraph!!.init()
            }
        }

        /**
         * This is how dependencies get injected, typically an Activity/Fragment/View will call this
         * during the onCreate()/onCreateView()/onFinishInflate() method respectively for each of the
         * dependencies it needs.
         *
         *
         * Can use the dagger library for similar behaviour using annotations
         *
         *
         * Will return mocks if they have been injected previously in injectMockObject()
         *
         *
         * Call it like this:  YourModel yourModel =
         * CustomApp.get(YourModel.class);
         *
         *
         * If you want to more tightly scoped object, pass a factory class here and create an instance
         * where you need it
         *
         * @param s
         * @return
         */
        operator fun <T> get(s: Class<T>): T {
            Affirm.notNull(objectGraph!!)
            return objectGraph!!.get(s)
        }
    }

}
