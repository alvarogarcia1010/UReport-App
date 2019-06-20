package com.agarcia.riskreporter.Fragments.NewReport


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation

import com.agarcia.riskreporter.R
import kotlinx.android.synthetic.main.fragment_image.*

class ImageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_image, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_2)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fr_image_bt_next.setOnClickListener {
            val nextAction = ImageFragmentDirections.nextAction(
                "hola1",
                "hola2",
                "hola3",
                "hola4",
                "hola5"
            )
            Navigation.findNavController(it).navigate(nextAction)
        }

    }


}
