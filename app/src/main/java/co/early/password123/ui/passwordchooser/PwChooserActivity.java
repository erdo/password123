package co.early.password123.ui.passwordchooser;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import co.early.password123.ui.common.BaseActivity;


public class PwChooserActivity extends BaseActivity {

    public static void start(Context context) {
        Intent intent = build(context);
        context.startActivity(intent);
    }

    public static Intent build(Context context) {
        Intent intent = new Intent(context, PwChooserActivity.class);
        return intent;
    }



    @Override
    public Fragment getFragmentInstance() {
        return PwChooserFragment.newInstance();
    }

    @Override
    public String getFragmentTag() {
        return PwChooserFragment.class.getSimpleName();
    }

}