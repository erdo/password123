package co.early.password123.ui.about;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.View;

import co.early.password123.ui.common.BaseActivity;


public class AboutActivity extends BaseActivity {


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
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    public Fragment getFragmentInstance() {
        return AboutFragment.newInstance();
    }

    @Override
    public String getFragmentTag() {
        return AboutFragment.class.getSimpleName();
    }

}