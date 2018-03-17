package co.early.password123.ui.common;


import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.early.password123.CustomApp;
import co.early.password123.R;


public abstract class BaseActivity extends AppCompatActivity {

    private int currentActivityScreenWidthPx;
    private int currentActivityScreenHeightPx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        CustomApp.init();

        setupScreenDimensions();

        setContentView(R.layout.common_activity_base);

        if (savedInstanceState == null) {
            setFragment(getFragmentInstance(), getFragmentTag());
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    private void setFragment(Fragment fragment, String fragmentTag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(
                R.id.content_main,
                fragment,
                fragmentTag);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public abstract Fragment getFragmentInstance();
    public abstract String getFragmentTag();

    private void setupScreenDimensions(){
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        currentActivityScreenWidthPx = size.x;
        currentActivityScreenHeightPx = size.y;
    }

    public int getCurrentActivityScreenWidthPx() {
        return currentActivityScreenWidthPx;
    }

    public int getCurrentActivityScreenHeightPx() {
        return currentActivityScreenHeightPx;
    }

    public float getDensityScalingFactor(){
        return getResources().getDisplayMetrics().density;
    }

}