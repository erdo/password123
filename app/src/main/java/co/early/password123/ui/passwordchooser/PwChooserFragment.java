package co.early.password123.ui.passwordchooser;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.early.password123.R;

public class PwChooserFragment extends Fragment {

    public static PwChooserFragment newInstance() {
        PwChooserFragment fragment = new PwChooserFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pwchooser, null);
    }

}
