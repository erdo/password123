package co.early.password123.ui.common;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import co.early.password123.CustomApp;
import co.early.password123.R;


public abstract class BaseActivity extends Activity {

    public static void startWithTransition(Activity activity, Pair<View, String>... sharedElements) {
        Intent intent = build(activity);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, sharedElements);
        activity.startActivity(intent, options.toBundle());
    }

    public static void start(Context context) {
        Intent intent = build(context);
        context.startActivity(intent);
    }

    public static Intent build(Context context) {
        Intent intent = new Intent(context, BaseActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // overridePendingTransition(0, 0);

        CustomApp.init();

        setContentView(R.layout.common_activity_base);

        if (savedInstanceState == null) {
            setFragment(getFragmentInstance(), getFragmentTag());
        }

      //  TransitionUtils.postponeTransition(this);

       // initialiseActivityTransition();
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
        TransitionUtils.setupSubLevelActivityTransitions(getWindow());
    }

}