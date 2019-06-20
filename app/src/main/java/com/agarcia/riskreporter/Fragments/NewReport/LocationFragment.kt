package com.agarcia.riskreporter.Fragments.NewReport


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.agarcia.riskreporter.Database.Models.Report

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.ReportViewModel
import kotlinx.android.synthetic.main.fragment_location.*
import java.lang.Exception

class LocationFragment : Fragment() {

    lateinit var title : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String
    lateinit var image: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.fragment_location, container, false)

        (activity as AppCompatActivity).supportActionBar?.subtitle = getString(R.string.step_3)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val safeArgs = LocationFragmentArgs.fromBundle(it)
            Log.d("hola", safeArgs.toString())
            title = safeArgs.title
            risk = safeArgs.riskLevel
            description = safeArgs.remark
            date = safeArgs.date
            image = safeArgs.urlImage
        }


        fr_location_bt_next.setOnClickListener {
            val nextAction = LocationFragmentDirections.nextAction(
                title,
                description,
                date,
                image,
                risk,
                fr_location_et_location.text.toString()
            )
            Navigation.findNavController(it).navigate(nextAction)
        }

        fr_location_date.text = date

    }

}
