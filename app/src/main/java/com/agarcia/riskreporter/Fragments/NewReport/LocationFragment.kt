package com.agarcia.riskreporter.Fragments.NewReport


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.agarcia.riskreporter.Activities.MainActivity
import com.agarcia.riskreporter.Database.Report

import com.agarcia.riskreporter.R
import com.agarcia.riskreporter.ViewModel.ReportViewModel
import kotlinx.android.synthetic.main.fragment_location.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class LocationFragment : Fragment() {

    lateinit var title : String
    lateinit var image : String
    lateinit var risk : String
    lateinit var description : String
    lateinit var date : String

    lateinit var reportViewModel : ReportViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val safeArgs = LocationFragmentArgs.fromBundle(it)
            Log.d("hola", safeArgs.toString())

            title = safeArgs.title
            image = safeArgs.urlImage
            risk = safeArgs.riskLevel
            description = safeArgs.description
            date = safeArgs.date
        }

        reportViewModel = ViewModelProviders.of(this).get(ReportViewModel::class.java)

        fr_location_bt_save.setOnClickListener {

            val report = Report(
                title,
                image,
                description,
                fr_location_et_location.text.toString(),
                risk,
                fr_location_et_measures.text.toString(),
                "Luis Castillo",
                date,
                "Pendiente"
            )

            try{
                reportViewModel.insertReport(report)
                val intent = Intent(view.context, MainActivity::class.java)
                startActivity(intent)
            }catch (e: Exception){
                Log.d("Codigo", e.message)
            }
        }

    }




}
