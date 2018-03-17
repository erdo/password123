package co.early.password123.ui.passwordchooser;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import co.early.password123.CustomApp;
import co.early.password123.ui.common.BaseActivity;
import co.early.pwned.Pwned;


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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Interesting thing happens here, because Pwned is so paranoid about not keeping passwords,
        // even in memory, it immediately hashes everything you give to it, that's why there is no
        // Pwned.getPassword() method. When a user presses back from the main screen, the view is
        // destroyed, but Android keeps the application around (and with it the Pwned instance).
        //
        // When the user then comes back, Pwned still has the result of the last search to provide
        // the view with, but cannot tell the view what password it relates to, and as the view
        // and edit text was destroyed but android, the original password is lost. The EditText
        // is populated with and empty string and then Pwned catches up, but there is a moment
        // when the result provided by Pwned does not match the edit text contents.
        //
        // Our solution(hack?) at the moment is to set the password to an empty string immediately
        // which is what it will be when we come back. Another solution might be to be less paranoid
        // about keeping the passwords in RAM and just provide a Pwned.getPassword() method
        CustomApp.get(Pwned.class).setPasswordToCheck("");
    }
}