package co.early.password123.ui.common;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import co.early.password123.CustomApp;
import co.early.password123.R;


public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        overridePendingTransition(0, 0);

        CustomApp.init();

        setContentView(R.layout.common_activity_base);

        if (savedInstanceState == null) {
            setFragment(getFragmentInstance(), getFragmentTag());
        }

//        TransitionUtils.postponeTransition(this);
//        initialiseActivityTransition();
    }

    private void setFragment(Fragment fragment, String fragmentTag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.content_main,
                fragment,
                fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public abstract Fragment getFragmentInstance();
    public abstract String getFragmentTag();

    /* Protected to allow subclasses to override with their own transition if needed. */
    protected void initialiseActivityTransition() {
        TransitionUtils.setupActivityTransitions1(getWindow());
    }

}