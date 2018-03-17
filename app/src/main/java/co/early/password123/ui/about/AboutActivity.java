package co.early.password123.ui.about;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import co.early.password123.ui.common.BaseActivity;


public class AboutActivity extends BaseActivity {


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