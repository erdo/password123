package co.early.password123.ui.passwordchooser

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import co.early.password123.R

class PwChooserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pwchooser, null)
    }

    companion object {
        fun newInstance(): PwChooserFragment {
            return PwChooserFragment()
        }
    }

}
