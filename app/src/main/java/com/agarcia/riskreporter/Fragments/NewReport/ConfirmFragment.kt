package com.agarcia.riskreporter.Fragments.NewReport


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_confirm.*
import kotlinx.android.synthetic.main.fragment_image.*

class ConfirmFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_confirm, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_4)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fr_confirm_bt_save.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.save_report)
            activity?.finish()
        }

    }

}
